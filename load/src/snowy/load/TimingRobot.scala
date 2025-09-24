package snowy.load

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContextExecutor
//import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol.{ClientPong, GameClientMessage}
import snowy.GameServerProtocol.ClientPing
import snowy.measures.Span

import scala.concurrent.duration.*
import snowy.measures.MeasurementRecorder
import scribe.Logging

/** A game client that sends ClientPing messages to the server
  * and measures how long it takes for the server to respond
  */
class TimingRobot(using ActorSystem, MeasurementRecorder)(url: String) extends Logging {
  implicit val dispatcher: ExecutionContextExecutor = implicitly[ActorSystem].dispatcher
  val period              = 1.second
  val gameSocket          = new GameSocket(url, messageReceived)
  var span                = Span.root("loadTest.ClientPing")

  gameSocket.connect()
  timePing()

  def messageReceived(message: GameClientMessage): Unit = {
    message match {
      case ClientPong =>
        logger.trace("ClientPong received")
        span.finishNow()
        timePing()
      case _ =>
    }
  }

  def timePing(): Unit = {
    implicitly[ActorSystem].scheduler.scheduleOnce(period) {
      span = span.restart()
      logger.trace("ClientPing sent")
      gameSocket.sendMessage(ClientPing)
    }
  }

  def shutdown(): Unit = {
    gameSocket.socket.close()
  }

}
