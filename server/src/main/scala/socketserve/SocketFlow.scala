package socketserve

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.Future
import akka.{Done, NotUsed}
import akka.actor._
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream._
import akka.stream.scaladsl._
import com.typesafe.scalalogging.StrictLogging
import socketserve.AppHost.Protocol._
import socketserve.FlowImplicits._

class SocketFlow(appHost: AppHost)(implicit system: ActorSystem) extends StrictLogging {
  val outputBufferSize = 1000
  val inputBufferSize  = 10

  val inputBufferLevel = new AtomicInteger()
  import system.dispatcher

  /** A flow for each connection received over the /game websocket
    * The flow is materialized by the WebServer */
  def messages(): Flow[Message, Message, NotUsed] = {
    val connectionId = new ConnectionId // user for this connection

    val (inputSink, messagesRefFuture) = setupInput(connectionId)

    // create an actor ref to accept and buffer messages sent to the client
    val (out, outRefFuture) =
      Source.actorRef[Message](outputBufferSize, OverflowStrategy.dropBuffer).peekMat

    reportOnOpen(connectionId, messagesRefFuture, outRefFuture)
    Flow.fromSinkAndSource(inputSink, out)
  }

  /** Setup the input stream.
    *
    * @return a Sink for incoming web socket messages (for the websocket input flow)
    * and a future actor ref for GameCommand messages (which the caller can use to
    * inject GameCommand messages from outside the flow.)
    */
  private def setupInput(
        connectionId: ConnectionId): (Sink[Message, NotUsed], Future[ActorRef]) = {
    val internalMessagesBuffer = 100

    // watch for closing the socket
    val (inputNotifyDone, terminationFutureMat) =
      Flow[Message].watchTermination()(Keep.right).peekMat

    // buffer input messages from the client
    var lastBufferLevel = 0
    val inputBuffered: Flow[Message, Message, NotUsed] =
      inputNotifyDone
        .mapMaterializedValue(_ => NotUsed)
        .foreach { _ =>
          val level = inputBufferLevel.incrementAndGet()
          if (level > 1 & level != lastBufferLevel) {
            lastBufferLevel = level
            logger.info(s"input buffer $connectionId  level: $level")
          }
        }
        .buffer(inputBufferSize, OverflowStrategy.dropBuffer)
        .foreach(_ => inputBufferLevel.decrementAndGet())
        .named("inputBuffered")

    // convert web socket messages into client controller messages
    val inputConverted: Flow[Message, GameCommand, NotUsed] =
      inputBuffered.collect {
        case BinaryMessage.Strict(data) => ClientMessage(connectionId, data)
      }.named("inputConverted")

    // target for sending connection open/close messages to the controller
    val (internalMessages, internalRefFuture) =
      Source
        .actorRef[GameCommand](internalMessagesBuffer, OverflowStrategy.fail)
        .named("inputInternalMessages")
        .peekMat

    reportOnClose(connectionId, terminationFutureMat, internalRefFuture)

    val controllerSink = appHost.messagesSink()
    val inputSink      = inputConverted.merge(internalMessages).to(controllerSink)
    (inputSink, internalRefFuture)
  }

  /** send a message the controller when the output actor is ready */
  private def reportOnOpen(connectionId: ConnectionId,
                           messagesRefFuture: Future[ActorRef],
                           outRefFuture: Future[ActorRef]): Unit = {

    /*
    We need an external-to-the-stream way to send messages to the client, so
    that the controller can push messages whenever it wants.
    . what we're doing here is to get a reference to output port (the outRef)
      . and then pass that reference to the controller input stream
        . which requires, similarly, an external-to-the-stream way of passing
          data into the controller input stream
        . so the code below links the two, passing the outRef to the controller input stream
     */

    for {
      messagesRef <- messagesRefFuture
      outRef      <- outRefFuture
    } {
      messagesRef ! Open(connectionId, outRef)
    }
  }

  private def reportOnClose(connectionId: ConnectionId,
                            terminationFutureMat: Future[Future[Done]],
                            internalRefFuture: Future[ActorRef]): Unit = {

    // report closing of the input stream
    for {
      terminationFuture <- terminationFutureMat
      messagesRef       <- internalRefFuture
    } {
      terminationFuture.onComplete { _ =>
        messagesRef ! Gone(connectionId)
      }
      terminationFuture.failed.foreach { err =>
        logger.warn(s"$connectionId didn't close normally: $err")
      }
    }
  }

}
