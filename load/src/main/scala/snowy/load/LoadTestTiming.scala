package snowy.load

import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, SourceQueueWithComplete}
import snowy.GameClientProtocol.{ClientPong, GameClientMessage}
import snowy.GameServerProtocol.{ClientPing, GameServerMessage}
import snowy.load.LoadTestTiming.{ACT, EC, MR}
import snowy.load.SnowyServerFixture.connectSinkToServer
import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging
import snowy.util.{MeasurementRecorder, Span, StartedSpan}

object LoadTestTiming {
  type EC[_]  = ExecutionContext
  type ACT[_] = ActorSystem
  type MR[_] = MeasurementRecorder
}

class LoadTestTiming[E: EC: ACT: MR](url: String) extends StrictLogging {
  var send: Option[SourceQueueWithComplete[GameServerMessage]] = None

  var span: StartedSpan = Span.start("loadTest.ClientPing")

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
