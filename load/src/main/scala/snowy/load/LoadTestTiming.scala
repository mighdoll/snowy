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
class LoadTestTiming[_: Actors: Measurement](url: String) extends StrictLogging {
  implicit val dispatcher = implicitly[ActorSystem].dispatcher
  var send: Option[SourceQueueWithComplete[GameServerMessage]] = None

  var span: StartedSpan = Span.root("loadTest.ClientPing")

  val sink = Sink.foreach[GameClientMessage] {
    case ClientPong =>
      logger.trace("ClientPong received")
      span.finish()
      timePing()
    case _ =>
  }

  for ((sendQ, _) <- connectSinkToServer(url, sink)) {
    send = Some(sendQ)
    timePing()
  }

  val period = 1.second

  def timePing(): Unit = {
    implicitly[ActorSystem].scheduler.scheduleOnce(period) {
      span = span.restart()
      logger.trace("ClientPing sent")
      send.foreach(_.offer(ClientPing))
    }
  }

  def shutdown(): Unit = {
    send.foreach(_.complete())
    send = None
  }

}
