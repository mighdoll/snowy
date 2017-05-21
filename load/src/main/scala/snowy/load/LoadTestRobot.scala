package snowy.load

import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol._
import snowy.GameServerProtocol.{GameServerMessage, Pong}
import snowy.robot.{Robot, RobotApi, RobotGameState}
import socketserve.ActorTypes._
import vector.Vec2d

/** Host for a single robot in a client, e.g. for a load test via a websocket.
  * Provides the RobotApi to the robot logic. Internally sends and
  * receives messages from the game server. */
class LoadTestRobot[_: Actors: Measurement](
      url: String
)(createRobot: (RobotApi => Robot))
    extends StrictLogging {

  private implicit val dispatcher = implicitly[ActorSystem].dispatcher
  private var hostedState         = RobotGameState.emptyGameState

  val connection = new GameSocket(url, receiveMessage)
  val api        = new HostedRobotApi(connection)
  val robot      = createRobot(api)

  connection.connect()

  def receiveMessage(message: GameClientMessage): Unit = {
    message match {
      case Ping =>
        api.sendToServer(Pong)
      case Died =>
        robot.killed()
      case PlayfieldBounds(width, height) =>
        hostedState = hostedState.copy(playfield = Vec2d(width, height))
      case InitialTrees(trees) =>
        hostedState = hostedState.copy(trees = trees)
      case MySled(sledId) =>
        logger.info(s"load robot joined. sledId $sledId")
        hostedState = hostedState.copy(mySledId = sledId)
      case state: State =>
        logger.trace(s"state $state")
        hostedState = hostedState.copy(
          allSleds = state.sleds,
          snowballs = state.snowballs
        )
        robot.refresh(hostedState)
      case _ =>
    }
  }
}

class HostedRobotApi(connection: GameSocket) extends RobotApi with StrictLogging {
  override def sendToServer(message: GameServerMessage): Unit = {
    logger.trace(s"HostedRobot sending message: $message")
    connection.sendMessage(message)
  }
}
