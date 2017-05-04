package snowy.load

import scala.concurrent.Promise
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, SourceQueue}
import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol._
import snowy.GameServerProtocol.{GameServerMessage, Pong}
import snowy.load.SnowyClientSocket.connectBlindSinkToServer
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
  private var promisedRobot = Promise[Robot]()
  private var promisedSend  = Promise[SourceQueue[GameServerMessage]]()
  private var hostedState   = RobotGameState.emptyGameState

  private val flow =
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

  private val sink: Sink[GameClientMessage, _] = flow.to(Sink.ignore)

  connectBlindSinkToServer(url, sink).foreach {
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
