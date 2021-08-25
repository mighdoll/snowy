package snowy.load

import akka.actor.ActorSystem
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.GameClientProtocol.{ClientPong, GameClientMessage}
import snowy.GameServerProtocol.ClientPing
import snowy.util.ActorTypes._
import scala.concurrent.duration._
import snowy.measures.Span

/** A game client that sends ClientPing messages to the server
  * and measures how long it takes for the server to respond
  */
class TimingRobot[_: Actors: Measurement](url: String) extends Logging {
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
