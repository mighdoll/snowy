package snowy.load

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, SourceQueueWithComplete}
import snowy.GameClientProtocol.{ClientPong, GameClientMessage}
import snowy.GameServerProtocol.{ClientPing, GameServerMessage}
import socketserve.ActorTypes._
import snowy.load.SnowyClientSocket.connectSinkToServer
import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging
import snowy.util.{Span, StartedSpan}

/** A game client that sends ClientPing messages to the server
  * and measures how long it takes for the server to respond */
class TimingRobot[_: Actors: Measurement](url: String) extends StrictLogging {
  implicit val dispatcher = implicitly[ActorSystem].dispatcher
  val period              = 1.second
  val gameSocket          = new GameSocket(url, messageReceived)
  var span                = Span.root("loadTest.ClientPing")

  gameSocket.connect()
  timePing()

  def messageReceived(message: GameClientMessage): Unit = {
    message match {
      case ClientPong =>
        logger.trace("ClientPong received")
        span.finish()
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
