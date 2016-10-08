package socketserve

import akka.NotUsed
import akka.actor._
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream._
import akka.stream.scaladsl._
import socketserve.AppHost.Protocol._

class SocketFlow(appHost: AppHost)(implicit system: ActorSystem) {
  val app = appHost.appActor
  val outputBufferSize = 100

  /** a flow for each connection received over the /game websocket */
  def messages(): Flow[Message, Message, NotUsed] = {
    val connectionId = new ConnectionId // user for this connection

    // create an actor ref to accept and buffer messages sent to the client
    val out = Source.actorRef[TextMessage](outputBufferSize, OverflowStrategy.dropTail)
      .mapMaterializedValue { outRef => app ! Open(connectionId, outRef) }

    // forward messages from the client, and note if the connection drops
    val in = Flow[Message]
      .collect {
        case TextMessage.Strict(text) => app ! ClientMessage(connectionId, text)
      }.to(Sink.actorRef[Unit](app, Gone(connectionId)))

    Flow.fromSinkAndSource(in, out)
  }
}
