package snowy.load

import scala.concurrent.{ExecutionContext, Promise}
import akka.stream.scaladsl.{Flow, Sink, SourceQueue}
import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol._
import snowy.GameServerProtocol.{GameServerMessage, Pong}
import snowy.load.SnowyServerFixture.connectSinkToServer
import snowy.robot.{Robot, RobotApi, RobotGameState}
import vector.Vec2d

/** Host for a single robot in a client, e.g. for a load test via a websocket.
  * Provides the RobotApi to the robot logic. Internally sends and
  * receives messages from the game server. */
class LoadTestRobot(
      url: String
)(createRobot: (RobotApi => Robot))(implicit executionContext: ExecutionContext)
    extends StrictLogging {

  var promisedRobot = Promise[Robot]()
  var promisedSend  = Promise[SourceQueue[GameServerMessage]]()
  var hostedState   = RobotGameState.emptyGameState

  val flow =
    Flow[GameClientMessage].map {
      case Ping =>
        promisedSend.future.foreach(_.offer(Pong))
      case Died =>
        promisedRobot.future.foreach(_.killed())
      case Playfield(width, height) =>
        hostedState = hostedState.copy(playfield = Vec2d(width, height))
      case Trees(trees) =>
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

        promisedRobot.future.foreach(_.refresh(hostedState))
      case _ =>
    }

  val sink: Sink[GameClientMessage, _] = flow.to(Sink.ignore)

  connectSinkToServer(url, sink).foreach {
    case (sendQueue, m) =>
      promisedSend.success(sendQueue)
      val api   = new HostedRobotApi(sendQueue)
      val robot = createRobot(api)
      promisedRobot.success(robot)
  }

}

class HostedRobotApi(sendQueue: SourceQueue[GameServerMessage])
    extends RobotApi with StrictLogging {
  override def sendToServer(message: GameServerMessage): Unit = {
    logger.trace(s"HostedRobot sending message: $message")
    sendQueue.offer(message)
  }
}
