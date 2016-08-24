import akka.NotUsed
import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn

import GameCommand._

class ProtoFlow(implicit system:ActorSystem) {
  val game = new Game().ref

  /** a flow for each connection received over the /game websocket */
  def messages(): Flow[Message, Message, NotUsed] = {
    val connectionId = new ConnectionId // user for this connection

    // create an actor ref to accept and buffer messages sent to the client
    val out =
      Source.actorRef[TextMessage](4, OverflowStrategy.fail)
        .mapMaterializedValue{outRef => game ! Join(connectionId, outRef) }

    // forward messages from the client, and note if the connection drops
    val in = Flow[Message].collect {
        case TextMessage.Strict(text) => game ! ClientMessage(connectionId, text)
      }.to(Sink.actorRef[Unit](game, Gone(connectionId)))

    Flow.fromSinkAndSource(in, out)
  }
}
