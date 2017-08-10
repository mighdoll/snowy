package snowy.server

import scala.collection.mutable
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.util.ByteString
import boopickle.DefaultBasic.{Pickle, Unpickle}
import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import snowy.measures.Span
import snowy.measures.Span.time
import snowy.playfield.Picklers._
import snowy.playfield.{Sled, _}
import snowy.robot.RobotPlayer
import snowy.server.ClientReporting.optNetId
import snowy.server.rewards.Achievements._
import socketserve._

/** Central controller for the game. Delegates protocol messages from clients,
  * and from the game framework. */
class GameControl(api: AppHostApi)(implicit system: ActorSystem, parentSpan: Span)
    extends AppController with GameState with StrictLogging {
  override val turnPeriod = 20 milliseconds
  val gameTurns           = new GameTurn(this, turnPeriod)

  private val messageIO     = new MessageIO(api)
  private val connections   = mutable.Map[ConnectionId, ClientConnection]()
  private val robots        = new RobotHost(this)
  private val commands      = new PersistentControls(gameStateImplicits)
  private val gameDebug     = new GameDebug(this, robots)
  private def connectionIds = connections.keys
  private val clientReport =
    new ClientReporting(messageIO, gameStateImplicits, connectionIds, robots)
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
      serverSled <- sledMap.get(connectionId)
    } {
      serverSled.sled.remove()
    }
    users.remove(connectionId)
    commands.pendingControls.commands.remove(connectionId)
    connections.remove(connectionId)
  }

  /** decode received binary message then pass on to handler */
  override def message(id: ConnectionId, msg: ByteString): Unit = {
    handleMessage(id, Unpickle[GameServerMessage].fromBytes(msg.asByteBuffer))
  }

  /** Run the next game turn. (called on a periodic timer) */
  override def tick(): Unit = {
    Span("GameControl.tick").finishSpan { implicit span =>
      val deltaSeconds = gameTurns.nextTurn()
      time("robotsTurn") { robots.robotsTurn() }
      commands.applyCommands(motion, snowballs, gameTime, deltaSeconds)
      val turnResults = gameTurns.turn(deltaSeconds)
      time("reportTurnResults") {
        clientReport.reportTurnResults(turnResults)
      }
      reapDeadSleds(turnResults.deadSleds)
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
      case TargetAngle(angle)          => targetDirection(id, angle)
      case Shoot(_)                    => shootOneSnowball(id)
      case Start(cmd, time)            => commands.startControl(id, cmd, time)
      case Stop(cmd, time)             => commands.stopControl(id, cmd, time)
      case Boost(time)                 => id.sled.foreach(boostSled(_, time))
      case Pong                        => optNetId(id).foreach(pong)
      case ReJoin                      => rejoin(id)
      case TestDie                     => reapSled(sledMap(id).sled)
      case DebugKey(key)               => gameDebug.debugCommand(id, key)
      case RequestGameTime(clientTime) => requestGameTime(id, clientTime)
      case ClientPing                  => optNetId(id).foreach(sendMessage(ClientPong, _))
    }
  }

  private def requestGameTime(clientId: ClientId, clientTime: Long): Unit = {
    for {
      netId      <- optNetId(clientId)
      connection <- connections.get(netId)
    } {
      clientReport.reportGameTime(netId, connection.roundTripTime)
    }
  }

  private def shootOneSnowball(id: ClientId): Unit = {
    for (sled <- id.sled) {
      commands.shootSnowball(sled, snowballs, gameTime)
    }
  }

  private def pong(netId: ConnectionId): Unit = {
    connections.get(netId).foreach(_.pongReceived())
  }

  /** Add some autonomous players to the game */
  private def robotSleds(): Unit = {
    val numRobotSleds = 2
    (1 to numRobotSleds).foreach { _ =>
      robots.createRobot(RobotPlayer.apply)
    }
  }

  /** update game clients with the current state of the game */
  private def sendUpdates(): Unit = {
    sendState()
    clientReport.sendScores(users, gameTime)
  }

  /** Send the current playfield state to the clients */
  private def sendState(): Unit = {
    val state = currentState()
    clientReport.sendToAllClients(state)
  }

  /** apply a push to a sled */
  private def boostSled(sled: Sled, clientTime: Long): Unit = {
    if (gameTime - sled.lastBoostTime >= sled.boostRecoveryTime * 1000) {
      sled.lastBoostTime = gameTime
      SledDrive.accelerate(sled, sled.boostAcceleration)
    }
  }

  /** Remove dead sleds from the game */
  private def reapDeadSleds(dead: Traversable[SledOut]): Unit = {
    for (SledOut(serverSled) <- dead) {
      serverSled.sled.remove()
    }
  }

  /** remove a dead sled and send a message (used by TestDie testing interface) */
  private def reapSled(sled: Sled): Unit = {
    clientReport.sendDied(sled.id)
    sled.remove()
  }

  private def newRandomSled(userName: String, sledType: SledType, color: SkiColor): Sled = {
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
    val user =
      new User(userName, createTime = gameTime, sledType = sledType, skiColor = skiColor)
    users(id) = user
    val sled = createSled(id, user, sledType)
    clientReport.joinedSled(id, sled.id)

    logger.info(
      s"user joined: $userName  connection: $id  sled: ${sled.id}  sledType: $sledType userCount:${users.size}"
    )
  }

  private def rejoin(id: ClientId): Unit = {
    users.get(id) match {
      case Some(user) =>
        val sled = createSled(id, user, user.sledType)
        logger.info(s"user rejoined: ${user.name}  ${sled.id}")
        clientReport.joinedSled(id, sled.id)
      case None =>
        logger.warn(s"user not found to rejoin: $id")
    }
  }

  private def createSled(connectionId: ClientId, user: User, sledType: SledType): Sled = {
    val sled = newRandomSled(user.name, sledType, user.skiColor)
    sleds.items.add(sled)
    sledMap(connectionId) = ServerSled(sled, user)
    sled
  }

  /** Point the sled in this direction */
  private def targetDirection(id: ClientId, angle: Double): Unit = {
    for (sled <- id.sled) {
      sled.turretRotation = -angle
    }
  }

}
