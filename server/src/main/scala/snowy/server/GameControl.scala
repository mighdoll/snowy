package snowy.server

import akka.actor.ActorSystem
import akka.util.ByteString
import boopickle.DefaultBasic.{Pickle, Unpickle}
import com.typesafe.scalalogging.StrictLogging
import snowy.Awards._
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import snowy.playfield.GameMotion._
import snowy.playfield.Picklers._
import snowy.playfield.PlayId.{BallId, PowerUpId, SledId}
import snowy.playfield.{Sled, _}
import snowy.robot.RobotPlayer
import snowy.server.CommonPicklers.withPickledClientMessage
import snowy.server.GameTurn.LevelUp
import snowy.util.Span.time
import snowy.util.{MeasurementRecorder, Span}
import socketserve._
import vector.Vec2d

import scala.collection.mutable
import scala.concurrent.duration._

class GameControl(api: AppHostApi)(implicit system: ActorSystem, parentSpan: Span)
    extends AppController with GameState with StrictLogging {
  override val turnPeriod = 20 milliseconds
  val gameTurns           = new GameTurn(this, turnPeriod)

  private val messageIO   = new MessageIO(api)
  private val connections = mutable.Map[ConnectionId, ClientConnection]()
  private val robots      = new RobotHost(this)
  private lazy val pickledTrees = {
    val message: GameClientMessage = InitialTrees(trees.items.toSeq)
    val bytes                      = Pickle.intoBytes(message)
    ByteString(bytes)
  }

  import gameStateImplicits._
  import gameTurns.gameTime
  import messageIO.{sendBinaryMessage, sendMessage}

  robotSleds()

  /** a new player has connected */
  override def open(id: ConnectionId): Unit = {
    logger.trace(s"open $id")
    connections(id) = new ClientConnection(id, messageIO)
    val clientPlayfield = PlayfieldBounds(playfield.size.x.toInt, playfield.size.y.toInt)
    sendMessage(clientPlayfield, id)
    sendBinaryMessage(pickledTrees, id)
    sendMessage(AddItems(powerUps.items.toSeq), id)
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
    pendingControls.commands.remove(connectionId)
    connections.remove(connectionId)
  }

  /** decode received binary message then pass on to handler */
  override def message(id: ConnectionId, msg: ByteString): Unit = {
    handleMessage(id, Unpickle[GameServerMessage].fromBytes(msg.asByteBuffer))
  }

  /** Run the next game turn. (called on a periodic timer) */
  override def tick(): Unit = {
    Span("GameControl.turn").finishSpan { implicit span =>
      val deltaSeconds = gameTurns.nextTurn()
      time("robotsTurn") { robots.robotsTurn() }
      applyTurn(deltaSeconds)
      applyDrive(deltaSeconds)
      applyCommands(deltaSeconds)
      val turnResults = gameTurns.turn(deltaSeconds)
      time("reportTurnResults") {
        reportLevelUps(turnResults.levelUps)
        reportSledKills(turnResults.sledKills)
        reapAndReportDeadSleds(turnResults.deadSleds)
        reportExpiredSnowballs(turnResults.deadSnowBalls)
        reportUsedPowerUps(turnResults.usedPowerUps)
        reportNewPowerUps(turnResults.newPowerUps)
      }
      time("sendUpdates") {
        sendUpdates()
      }
    }
  }

  /** Process a GameServerMessage from a game client browser or robot */
  def handleMessage(id: ClientId, msg: GameServerMessage): Unit = {
    logger.trace(s"handleMessage: $msg received from client $id")
    msg match {
      case Join(name, sledType, skiColor) =>
        userJoin(id, name.slice(0, 15), sledType, skiColor)
      case TargetAngle(angle) => targetDirection(id, angle)
      case Shoot(time)        => id.sled.foreach(shootSnowball(_))
      case Start(cmd, time)   => startControl(id, cmd, time)
      case Stop(cmd, time)    => stopControl(id, cmd, time)
      case Boost(time)        => id.sled.foreach(boostSled(_, time))
      case Pong               => optNetId(id).foreach(pong(_))
      case ReJoin             => rejoin(id)
      case TestDie            => reapSled(sledMap(id))
      case DebugKey(key)      => debugCommand(id, key)
      case RequestGameTime(clientTime) =>
        optNetId(id).foreach(reportGameTime(_, clientTime))
      case ClientPing => optNetId(id).foreach(sendMessage(ClientPong, _))
    }
  }

  /** client has started to operate a sled control. e.g. shooting, braking */
  private def startControl(id: ClientId, cmd: StartStopControl, time: Long): Unit = {
    cmd match {
      case persistentControl: PersistentControl =>
        pendingControls.startCommand(id, persistentControl, time)
      case driveControl: DriveControl =>
        for (sled <- id.sled) {
          driveControl match {
            case Coasting => sled.driveMode.driveMode(SledDrive.Coasting)
            case Slowing  => sled.driveMode.driveMode(SledDrive.Braking)
          }
        }
    }
  }

  /** client has stopped a sled control. e.g. shooting, braking */
  private def stopControl(id: ClientId, cmd: StartStopControl, time: Long): Unit = {
    cmd match {
      case persistentControl: PersistentControl =>
        pendingControls.stopCommand(id, persistentControl, time)
      case driveControl: DriveControl =>
        for (sled <- id.sled) {
          sled.driveMode.driveMode(SledDrive.Driving)
        }
    }
  }

  private def optNetId(id: ClientId): Option[ConnectionId] = {
    id match {
      case netId: ConnectionId => Some(netId)
      case i: RobotId          => None
    }
  }

  private def pong(netId: ConnectionId): Unit = {
    connections.get(netId).foreach(_.pongReceived())
  }

  /** Add some autonomous players to the game */
  private def robotSleds(): Unit = {
    (1 to 2).foreach { _ =>
      robots.createRobot(RobotPlayer.apply)
    }
  }

  /** update game clients with the current state of the game */
  private def sendUpdates(): Unit = {
    sendState()
    sendScores()
  }

  /** Send the current playfield state to the clients */
  private def sendState(): Unit = {
    val state = currentState()
    sendToAllClients(state)
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

  private def applyTurn(deltaSeconds: Double): Unit = {
    for (sled <- sleds.items) {
      val tau             = math.Pi * 2
      val distanceBetween = (sled.turretRotation - sled.rotation) % tau
      val wrapping        = (distanceBetween % tau + (math.Pi * 3)) % tau - math.Pi
      val dir             = math.round(wrapping * 10).signum // precision of when to stop
      sled.rotation += deltaSeconds * dir * sled.rotationSpeed
    }
  }

  /** apply any pending but not yet cancelled commands from user actions,
    * e.g. turning or slowing */
  private def applyDrive(deltaSeconds: Double): Unit = {
    for (sled <- sleds.items) {
      sled.driveMode.driveSled(sled, deltaSeconds)
    }
  }

  /** apply any pending but not yet cancelled commands from user actions,
    * e.g. turning or slowing */
  private def applyCommands(deltaSeconds: Double): Unit = {

    pendingControls.foreachCommand { (id, command, time) =>
      id.sled.foreach { sled =>
        command match {
          case Left     => motion.turnSled(sled, LeftTurn, deltaSeconds)
          case Right    => motion.turnSled(sled, RightTurn, deltaSeconds)
          case Shooting => shootSnowball(sled)
        }
      }
    }
  }

  private def reportSledKills(sledKills: Traversable[SledKill]): Unit = {
    for {
      SledKill(killerSledId, deadSledId) <- sledKills
    } {
      reportKiller(killerSledId, deadSledId)
      reportDeadSled(killerSledId, deadSledId)
    }

    def reportKiller(killerSledId: SledId, deadSledId: SledId): Unit = {
      for {
        killerClientId     <- killerSledId.connectionId
        killerConnectionId <- optNetId(killerClientId)
      } {
        val killedSled = KilledSled(deadSledId)
        sendMessage(killedSled, killerConnectionId)
      }
    }

    def reportDeadSled(killerSledId: SledId, deadSledId: SledId): Unit = {
      for {
        deadClientId     <- deadSledId.connectionId
        deadConnectionId <- optNetId(deadClientId)
      } {
        val killedBy = KilledBy(killerSledId)
        sendMessage(killedBy, deadConnectionId)
      }
    }

  }

  private def reportLevelUps(levelUps: Traversable[LevelUp]): Unit = {
    for {
      LevelUp(clientId, newLevel) <- levelUps
      connectionId                <- optNetId(clientId)
    } {
      val message = Notification(s"Level $newLevel !")
      sendMessage(message, connectionId)
    }
  }
  private def reportNewPowerUps(newPowerUps: Traversable[PowerUp]): Unit = {
    if (newPowerUps.nonEmpty) {
      val newItems = AddItems(newPowerUps.toSeq)
      sendToAllClients(newItems)
    }
  }

  private def reportUsedPowerUps(usedPowerUps: Traversable[PowerUpId]): Unit = {
    if (usedPowerUps.nonEmpty) {
      val removeItems = RemoveItems(PowerUpItem, usedPowerUps.toSeq)
      sendToAllClients(removeItems)
    }
  }

  private def reportExpiredSnowballs(expiredBalls: Traversable[BallId]): Unit = {
    if (expiredBalls.nonEmpty) {
      val deaths = RemoveItems(SnowballItem, expiredBalls.toSeq)
      sendToAllClients(deaths)
    }
  }

  /** apply a push to a sled */
  private def boostSled(sled: Sled, clientTime: Long): Unit = {
    if (gameTime - sled.lastBoostTime >= sled.boostRecoveryTime * 1000) {
      sled.lastBoostTime = gameTime
      SledDrive.accelerate(sled, sled.boostAcceleration)
    }
  }

  private def shootSnowball(
        sled: Sled
  )(implicit snowballTracker: PlayfieldTracker[Snowball]): Unit = {
    if (sled.lastShotTime + sled.minRechargeTime < gameTime) {
      val launchAngle    = sled.turretRotation + sled.bulletLaunchAngle
      val launchDistance = sled.bulletLaunchPosition.length + sled.radius
      val launchPos      = sled.bulletLaunchPosition.rotate(launchAngle).unit * launchDistance
      val direction      = Vec2d.fromRotation(launchAngle)
      val ball = new Snowball(
        ownerId = sled.id,
        speed = sled.speed + (direction * sled.bulletSpeed),
        radius = sled.bulletRadius,
        mass = sled.bulletMass,
        spawned = gameTime,
        impactDamage = sled.bulletImpactFactor,
        health = sled.bulletHealth,
        lifetime = sled.bulletLifetime
      )
      snowballs.addBall(ball, playfield.wrapInPlayfield(sled.position + launchPos))

      val recoilForce = direction * -sled.bulletRecoil
      sled.speed = sled.speed + recoilForce
      sled.lastShotTime = gameTime
    }
  }

  // TODO Notify clients who they kill, and who killed them
  /** Notify clients about sleds that have been killed, remove sleds from the game */
  private def reapAndReportDeadSleds(dead: Traversable[SledDied]): Unit = {
    val deadSleds =
      dead.map {
        case SledDied(sledId) => sledId
      }.toSeq

    if (deadSleds.nonEmpty) {
      val deaths = RemoveItems(SledItem, deadSleds)
      sendToAllClients(deaths)

      for { sledId <- deadSleds; sled <- sledId.sled } {
        sendDied(sledId)
        val connectIdStr =
          sledId.connectionId.map(id => s"(connection: $id) ").getOrElse("")
        logger.info(s"sled ${sledId.id} killed $connectIdStr sledCount:${sledMap.size}")
        sled.remove()
      }
    }
  }

  private def reapSled(sledId: SledId): Unit = {
    sendDied(sledId)
    sledId.sled.foreach(_.remove())
  }

  private def sendToAllClients(message: GameClientMessage): Unit = {
    withPickledClientMessage(message) { pickledState =>
      connections.keys.foreach { connectionId =>
        sendBinaryMessage(pickledState, connectionId)
      }
    }
  }

  private def sendDied(sledId: SledId): Unit = {
    sledId.connectionId match {
      case Some(netId: ConnectionId) => sendMessage(Died, netId)
      case Some(robotId: RobotId)    => robots.died(robotId)
      case None                      => logger.warn(s"reapSled connection not found for sled: $sledId")
    }
  }

  private def reportJoinedSled(connectionId: ClientId, sledId: SledId): Unit = {
    connectionId match {
      case robotId: RobotId    => robots.joined(robotId, sledId)
      case netId: ConnectionId => sendMessage(MySled(sledId), netId)
    }
  }

  private def newRandomSled(userName: String,
                            sledType: SledType,
                            color: SkiColor): Sled = {
    // TODO what if sled is initialized atop a tree?
    Sled(
      userName = userName,
      initialPosition = playfield.randomSpot(),
      sledType = sledType,
      color = color
    )
  }

  /** Called when a user sends her name and starts in the game */
  private def userJoin(id: ClientId,
                       userName: String,
                       sledType: SledType,
                       skiColor: SkiColor): Unit = {
    logger.info(
      s"user joined: $userName  id: $id  sledType: $sledType userCount:${users.size}"
    )
    val user =
      new User(userName, createTime = gameTime, sledType = sledType, skiColor = skiColor)
    users(id) = user
    val sled = createSled(id, user, sledType)
    reportJoinedSled(id, sled.id)
  }

  private def rejoin(id: ClientId): Unit = {
    users.get(id) match {
      case Some(user) =>
        logger.info(s"user rejoined: ${user.name}")
        val sled = createSled(id, user, user.sledType)
        reportJoinedSled(id, sled.id)
      case None =>
        logger.warn(s"user not found to rejoin: $id")
    }
  }

  private def createSled(connectionId: ClientId, user: User, sledType: SledType): Sled = {
    val sled = newRandomSled(user.name, sledType, user.skiColor)
    sleds.items.add(sled)
    sledMap(connectionId) = sled.id
    sled
  }

  /** Point the sled in this direction */
  private def targetDirection(id: ClientId, angle: Double): Unit = {
    id.sled.foreach { sled =>
      sled.turretRotation = -angle
    }
  }

  lazy val clientDebugEnabled = GlobalConfig.snowy.getBoolean("client-debug-messages")
  private def debugCommand(id: ClientId, key: Char): Unit = {
    if (clientDebugEnabled) {
      key match {
        case 'T' => logNearbyTrees(id)
        case x   => logger.warn(s"debug key $x not recognized from client $id")
      }
    }
  }

  private def logNearbyTrees(id: ClientId): Unit = {
    debugVerifyGridState()
    debugVerifyTreeState()

    for { sled <- id.sled } {
      logger.warn(s"logNearbyTrees.sled: $sled  position: ${sled.position}")
      val bounds = sled.boundingBox
      val bigger = Rect(bounds.pos - Vec2d(25, 25), bounds.size + Vec2d(50, 50))
      for { tree <- trees.grid.inside(bigger) } {
        logger.warn(s"logNearbyTrees.tree: $tree  position: ${tree.position}")
      }
    }
  }

}
