package socketserve

import java.util.concurrent.atomic.AtomicInteger
import akka.NotUsed
import akka.actor._
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream._
import akka.stream.scaladsl._
import com.typesafe.scalalogging.StrictLogging
import socketserve.AppHost.Protocol._

class SocketFlow(appHost: AppHost)(implicit system: ActorSystem) extends StrictLogging {
  val app              = appHost.appActor
  val outputBufferSize = 1000
  val inputBufferSize  = 10

  val inputBufferLevel = new AtomicInteger()

  implicit class FlowOps[In, Out, Mat](flow: Flow[In, Out, Mat]) {

    /** run a side effecting function on messages in the flow */
    def foreach(fn: Out => Unit): Flow[In, Out, Mat] = {
      flow.map { m =>
        fn(m)
        m
      }
    }
  }

  /** a flow for each connection received over the /game websocket */
  def messages(): Flow[Message, Message, NotUsed] = {
    val connectionId = new ConnectionId // user for this connection

    // create an actor ref to accept and buffer messages sent to the client
    val out = Source
      .actorRef[Message](outputBufferSize, OverflowStrategy.dropBuffer)
      .mapMaterializedValue { outRef =>
        app ! Open(connectionId, outRef)
      }

    val bufferMessages = Flow[Message].foreach { _ =>
      val size = inputBufferLevel.incrementAndGet()
      if (size > 1) logger.info(s"input buffer size: $size")
    }.buffer(inputBufferSize, OverflowStrategy.dropBuffer)
      .foreach(_ => inputBufferLevel.decrementAndGet())

    // forward messages from the client, and note if the connection drops
    val in = bufferMessages.collect {
      case TextMessage.Strict(text) =>
        app ! ClientMessage(connectionId, text)
      case BinaryMessage.Strict(text) =>
        app ! ClientBinaryMessage(connectionId, text)
    }.to(Sink.actorRef[Unit](app, Gone(connectionId)))

    Flow.fromSinkAndSource(in, out)
  }
}
