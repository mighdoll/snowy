package snowy.server

import scala.collection.mutable
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.util.ByteString
import boopickle.Default._
import com.typesafe.scalalogging.StrictLogging
import snowy.Awards._
import snowy.GameClientProtocol._
import snowy.GameConstants.Friction.slowButtonFriction
import snowy.GameConstants._
import snowy.GameServerProtocol._
import snowy.playfield.GameMotion._
import snowy.playfield.PlayId.SledId
import snowy.playfield.{Sled, _}
import snowy.robot.StationaryRobot
import snowy.server.GameSeeding.randomSpot
import snowy.util.Perf.time
import socketserve._
import vector.Vec2d

class GameControl(api: AppHostApi)(implicit system: ActorSystem)
    extends AppController with GameState with StrictLogging {
  override val turnPeriod = 20 milliseconds
  val messageIO           = new MessageIO(api)
  val connections         = mutable.Map[ConnectionId, ClientConnection]()
  val gameTurns           = new GameTurn(this, turnPeriod)
  val robots              = new RobotHost(this)

  import gameStateImplicits._
  import gameTurns.gameTime
  import messageIO.sendMessage

  robotSleds()

  /** a new player has connected */
  override def open(id: ConnectionId): Unit = {
    logger.trace(s"open $id")
    connections(id) = new ClientConnection(id, messageIO)
    val clientPlayfield = Playfield(playfield.x.toInt, playfield.y.toInt)
    sendMessage(clientPlayfield, id)
    sendMessage(Trees(trees.toSeq), id)
  }

  /** Called when a connection is dropped */
  override def gone(connectionId: ConnectionId): Unit = {
    logger.info(s"gone $connectionId")
    for {
      sledId <- sledMap.get(connectionId)
      sled   <- sledId.sled
    } {
      sled.remove()
    }
    users.remove(connectionId)
    commands.commands.remove(connectionId)
    connections.remove(connectionId)
  }

  /** decode received binary message then pass on to handler */
  override def message(id: ConnectionId, msg: ByteString): Unit = {
    handleMessage(id, Unpickle[GameServerMessage].fromBytes(msg.asByteBuffer))
  }

  /** Run the next game turn. (called on a periodic timer) */
  override def turn(): Unit = {
    val deltaSeconds = gameTurns.nextTurn()
    robots.robotsTurn()
    applyCommands(deltaSeconds)
    val died = gameTurns.turn(deltaSeconds)
    reapDead(died)
    time("sendUpdates") {
      sendUpdates()
    }
  }

  /** Process a GameServerMessage from a game client browser or robot */
  def handleMessage(id: ClientId, msg: GameServerMessage): Unit = {
    logger.trace(s"handleMessage: $msg received from client $id")
    msg match {
      case Join(name, sledKind, skiColor) =>
        val sled = userJoin(id, name.slice(0, 15), sledKind, skiColor)
        reportJoinedSled(id, sled.id)
      case TurretAngle(angle)          => rotateTurret(id, angle)
      case TargetAngle(angle)          => targetDirection(id, angle)
      case Shoot(time)                 => id.sled.foreach(sled => shootSnowball(sled))
      case Push(time)                  => id.sled.foreach(sled => pushSled(sled))
      case Start(cmd, time)            => commands.startCommand(id, cmd, time)
      case Stop(cmd, time)             => commands.stopCommand(id, cmd, time)
      case Pong                        => netIdForeach(id)(connections(_).pongReceived())
      case ReJoin                      => rejoin(id)
      case TestDie                     => reapSled(sledMap(id))
      case RequestGameTime(clientTime) => netIdForeach(id)(reportGameTime(_, clientTime))
    }
  }

  private def netIdForeach(id: ClientId)(fn: ConnectionId => Unit): Unit = {
    id match {
      case netId: ConnectionId => fn(netId)
      case i: RobotId          =>
    }
  }

  /** Add some autonomous players to the game */
  private def robotSleds(): Unit = {
    (1 to 10).foreach { _ =>
      robots.createRobot(StationaryRobot.apply)
    }
  }

  private def sendUpdates(): Unit = {
    val state = currentState()
    connections.keys.foreach { connectionId =>
      sendMessage(state, connectionId)
    }
    sendScores()
  }

  /** Send the current score to the clients */
  private def sendScores(): Unit = {
    val scores = {
      val rawScores = users.values.map { user =>
        Score(user.name, user.score)
      }.toSeq
      val sorted = rawScores.sortWith { (a, b) =>
        a.score > b.score
      }
      sorted.take(10)
    }
    users.collect {
      case (id: ConnectionId, user) if user.timeToSendScore(gameTime) =>
        user.scoreSent(gameTime)
        val scoreboard = Scoreboard(user.score, scores)
        sendMessage(scoreboard, id)
    }
  }

  private def reportGameTime(netId: ConnectionId, clientTime: Long): Unit = {
    logger.trace {
      val clientTimeDelta = clientTime - System.currentTimeMillis()
      s"client $netId time vs server time: $clientTimeDelta"
    }

    connections.get(netId) match {
      case Some(connection) => reportGameTime(connection.roundTripTime)
      case None             => logger.warn(s"reportGameTime: connection $netId not fouud")
    }

    def reportGameTime(rtt: Long): Unit = {
      val msg = GameTime(System.currentTimeMillis(), (rtt / 2).toInt)
      messageIO.sendMessage(msg, netId)
    }
  }

  /** apply any pending but not yet cancelled commands from user actions,
    * e.g. turning or slowing */
  private def applyCommands(deltaSeconds: Double): Unit = {

    commands.foreachCommand { (id, command, time) =>
      id.sled.foreach { sled =>
        command match {
          case Left  => turnSled(sled, LeftTurn, deltaSeconds)
          case Right => turnSled(sled, RightTurn, deltaSeconds)
          case Slowing =>
            val slow = new InlineForce(
              -slowButtonFriction * deltaSeconds / sled.mass,
              sled.maxSpeed)
            sled.speed = slow(sled.speed)
          case TurretLeft =>
            id.sled.foreach(_.turretRotation -= (math.Pi / turnTime) * deltaSeconds)
          case TurretRight =>
            id.sled.foreach(_.turretRotation += (math.Pi / turnTime) * deltaSeconds)
          case Shooting => shootSnowball(sled)
        }
      }
    }
  }

  /** apply a push to a sled */
  private def pushSled(sled: Sled): Unit = {
    val pushForceNow = PushEnergy.force / sled.mass

    if (sled.pushEnergy >= (1.0 / PushEnergy.maxAmount)) {
      val pushVector = Vec2d.fromRotation(-sled.turretRotation) * pushForceNow
      val rawSpeed= sled.speed + pushVector
      sled.speed = rawSpeed.clipLength(sled.maxSpeed)
      sled.pushEnergy = sled.pushEnergy - (1.0 / PushEnergy.maxAmount)
    }
  }

  private def shootSnowball(sled: Sled): Unit = {
    if (sled.lastShotTime + sled.minRechargeTime < gameTime) {
      val launchAngle = sled.turretRotation + sled.bulletLaunchAngle
      val launchPos   = sled.bulletLaunchPosition.rotate(-sled.turretRotation)
      val direction   = Vec2d.fromRotation(-launchAngle)
      val ball = Snowball(
        ownerId = sled.id,
        _position = wrapInPlayfield(sled.pos + launchPos), // TODO don't use _position
        speed = sled.speed + (direction * sled.bulletSpeed),
        radius = sled.bulletRadius,
        mass = sled.bulletMass,
        spawned = gameTime,
        impactDamage = sled.bulletImpactFactor,
        health = sled.bulletHealth,
        lifetime = sled.bulletLifetime
      )
      snowballs = snowballs.add(ball)

      val recoilForce = direction * -sled.bulletRecoil
      sled.speed = sled.speed + recoilForce
      sled.lastShotTime = gameTime
    }
  }

  /** Notify clients whose sleds have been killed, remove sleds from the game */
  private def reapDead(dead: Traversable[SledDied]): Unit = {
    dead.foreach {
      case SledDied(sledId) =>
        reapSled(sledId)
    }
  }

  private def reapSled(sledId: SledId): Unit = {
    sledId.connectionId match {
      case Some(netId: ConnectionId) => sendMessage(Died, netId)
      case Some(robotId: RobotId)    => robots.died(robotId)
      case None                      => logger.warn(s"reapSled connection not found for sled: $sledId")
    }
    sledId.sled.foreach(_.remove())
    logger.info(s"sled ${sledId.id} killed: sledCount:${sledMap.size}")
  }

  private def reportJoinedSled(connectionId: ClientId, sledId: SledId): Unit = {
    connectionId match {
      case robotId: RobotId    => robots.joined(robotId, sledId)
      case netId: ConnectionId => sendMessage(MySled(sledId), netId)
    }
  }

  private def newRandomSled(userName: String,
                            sledKind: SledKind,
                            color: SkiColor): Sled = {
    // TODO what if sled is initialized atop a tree?
    Sled(
      userName = userName,
      initialPosition = randomSpot(),
      kind = sledKind,
      color = color
    )
  }

  /** Called when a user sends her name and starts in the game */
  def userJoin(id: ClientId,
               userName: String,
               sledKind: SledKind,
               skiColor: SkiColor): Sled = {
    logger.info(
      s"user joined: $userName  id:$id  kind: $sledKind  sserCount:${users.size}")
    val user =
      new User(userName, createTime = gameTime, sledKind = sledKind, skiColor = skiColor)
    users(id) = user
    createSled(id, user, sledKind)
  }

  def rejoin(id: ClientId): Option[Sled] = {
    users.get(id) match {
      case Some(user) =>
        logger.info(s"user rejoined: ${user.name}")
        val sled = createSled(id, user, user.sledKind)
        Some(sled)
      case None =>
        logger.warn(s"user not found to rejoin: $id")
        None
    }
  }

  private def createSled(connectionId: ClientId, user: User, sledKind: SledKind): Sled = {
    val sled = newRandomSled(user.name, sledKind, user.skiColor)
    sleds = sleds.add(sled)
    sledMap(connectionId) = sled.id
    sled
  }

  /** Rotate the turret on a sled */
  private def rotateTurret(id: ClientId, angle: Double): Unit = {
    id.sled.foreach(_.turretRotation = angle)
  }

  /** Point the sled in this direction */
  private def targetDirection(id: ClientId, angle: Double): Unit = {
    id.sled.foreach { sled =>
      sled.rotation = -angle
    }
  }

}
