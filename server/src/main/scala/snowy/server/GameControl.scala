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
import snowy.server.GameSeeding.randomSpot
import snowy.util.Perf.time
import socketserve.{AppController, AppHostApi, ConnectionId}
import upickle.default._
import vector.Vec2d

class GameControl(api: AppHostApi)(implicit system: ActorSystem)
    extends AppController with GameState with StrictLogging {
  override val turnPeriod = 20 milliseconds
  val messageIO           = new MessageIO(api)
  val connections         = mutable.Map[ConnectionId, ClientConnection]()
  val gameTurns           = new GameTurn(this, turnPeriod)

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
  override def message(id: ConnectionId, msg: String): Unit = {
    handleMessage(id, read[GameServerMessage](msg))
  }

  /** decode received binary message then pass on to handler */
  override def binaryMessage(id: ConnectionId, msg: ByteString): Unit = {
    handleMessage(id, Unpickle[GameServerMessage].fromBytes(msg.asByteBuffer))
  }

  /** Run the next game turn. (called on a periodic timer) */
  override def turn(): Unit = {
    val deltaSeconds = gameTurns.nextTurn()
    logger.trace(s"tick $deltaSeconds")
    applyCommands(deltaSeconds)
    val died = gameTurns.turn(deltaSeconds)
    reapDead(died)
    time("sendUpdates") {
      sendUpdates()
    }
  }

  /** Process a GameServerMessage from the client */
  private def handleMessage(id: ConnectionId, msg: GameServerMessage): Unit = {
    logger.trace(s"handleMessage: $msg received from client $id")
    msg match {
      case Join(name, sledKind, skiColor) => userJoin(id, name, sledKind, skiColor)
      case TurretAngle(angle)             => rotateTurret(id, angle)
      case Shoot(time)                    => id.sled.foreach(sled => shootSnowball(sled))
      case Start(cmd, time)               => commands.startCommand(id, cmd, time)
      case Stop(cmd, time)                => commands.stopCommand(id, cmd, time)
      case Pong                           => connections(id).pongReceived()
      case ReJoin                         => rejoin(id)
      case TestDie                        => reapSled(sledMap(id))
      case RequestGameTime(clientTime)    => reportGameTime(id, clientTime)
    }
  }

  /** Add some autonomous players to the game */
  private def robotSleds(): Unit = {
    (1 to 20).foreach { i =>
      userJoin(
        id = new ConnectionId,
        userName = s"StationarySled:$i",
        sledKind = StationaryTestSled,
        skiColor = BasicSkis,
        robot = true
      )
    }

  }

  private def sendUpdates(): Unit = {
    currentState().collect {
      // TODO pickle sleds and snowall once, not once per connection
      case (id, state) if state.mySled.id.user.exists(!_.robot) =>
        sendMessage(state, id)
    }
    sendScores() // TODO send less often
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
      case (id, user) if !user.robot && user.timeToSendScore(gameTime) =>
        user.scoreSent(gameTime)
        val scoreboard = Scoreboard(user.score, scores)
        sendMessage(scoreboard, id)
    }
  }

  private def reportGameTime(id: ConnectionId, clientTime: Long): Unit = {
    logger.trace {
      val clientTimeDelta = clientTime - System.currentTimeMillis()
      s"client $id time vs server time: $clientTimeDelta"
    }

    connections.get(id) match {
      case Some(connection) => reportGameTime(connection.roundTripTime)
      case None             => logger.warn(s"reportGameTime: connection $id not fouud")
    }

    def reportGameTime(rtt: Long): Unit = {
      val msg = GameTime(System.currentTimeMillis(), (rtt / 2).toInt)
      messageIO.sendMessage(msg, id)
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
          case Pushing =>
            val pushForceNow = PushEnergy.force * deltaSeconds / sled.mass
            val pushEffort   = deltaSeconds / PushEnergy.maxTime
            val push         = new InlineForce(pushForceNow, sled.maxSpeed)
            pushSled(sled, pushForceNow, push, pushEffort)
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
  private def pushSled(sled: Sled,
                       pushForceNow: Double,
                       push: InlineForce,
                       effort: Double): Unit = {
    if (effort < sled.pushEnergy) {
      val speed =
        if (sled.speed.zero) Vec2d.fromRotation(sled.rotation) * pushForceNow
        else push(sled.speed)
      sled.speed = speed
      sled.pushEnergy = sled.pushEnergy - effort
    }
  }

  private def shootSnowball(sled: Sled): Unit = {
    if (sled.lastShotTime + sled.minRechargeTime < gameTime) {
      val launchAngle = sled.turretRotation + sled.bulletLaunchAngle
      val launchPos   = sled.bulletLaunchPosition.rotate(sled.turretRotation)
      val direction   = Vec2d.fromRotation(-launchAngle)
      val ball = Snowball(
        ownerId = sled.id,
        _position = wrapInPlayfield(sled.pos + launchPos), // TODO don't use _position
        speed = sled.speed + (direction * sled.bulletSpeed),
        radius = sled.bulletRadius,
        spawned = gameTime,
        impactDamage = sled.bulletImpactFactor,
        health = sled.bulletHealth
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
    if (sledId.user.exists(!_.robot)) {
      val connectionId = sledId.connectionId
      connectionId.foreach(sendMessage(Died, _))
    }
    sledId.sled.foreach(_.remove())
    logger.info(s"sled ${sledId.id} killed: sledCount:${sledMap.size}")
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
  private def userJoin(id: ConnectionId,
                       userName: String,
                       sledKind: SledKind,
                       skiColor: SkiColor,
                       robot: Boolean = false): Unit = {
    logger.info(
      s"user joined: $userName  kind: $sledKind  robot: $robot  userCount:${users.size}")
    val user =
      new User(
        userName,
        createTime = gameTime,
        sledKind = sledKind,
        skiColor = skiColor,
        robot = robot)
    users(id) = user
    createSled(id, user, sledKind)
  }

  private def rejoin(id: ConnectionId): Unit = {
    users.get(id) match {
      case Some(user) =>
        logger.info(s"user rejoined: ${user.name}")
        createSled(id, user, user.sledKind)
      case None =>
        logger.warn(s"user not found to rejoin: $id")
    }
  }

  private def createSled(connctionId: ConnectionId,
                         user: User,
                         sledKind: SledKind): Unit = {
    val sled = newRandomSled(user.name, sledKind, user.skiColor)
    sleds = sleds.add(sled)
    sledMap(connctionId) = sled.id
  }

  /** Rotate the turret on a sled */
  private def rotateTurret(id: ConnectionId, angle: Double): Unit = {
    id.sled.foreach(_.turretRotation = angle)
  }

}
