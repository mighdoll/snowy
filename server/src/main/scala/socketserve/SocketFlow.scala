package socketserve

import akka.actor._
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream._
import akka.stream.scaladsl._
import akka.{Done, NotUsed}
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.util.ActorUtil.materializerWithLogging
import socketserve.AppHost.Protocol._
import snowy.util.FlowImplicits._
import scala.concurrent.Future
import snowy.measures.{Gauged, Span}
import snowy.measures.Span

class SocketFlow(appHost: AppHost)(implicit system: ActorSystem, parentSpan: Span)
    extends Logging {
  val outputBufferSize              = 1000
  val inputBufferSize               = 100
  val internalMessagesSize          = 100
  private implicit val materializer = materializerWithLogging(logger)
  import system.dispatcher

  /** A flow for each connection received over the /game WebSocket
    * The flow is materialized by the WebServer */
  def messages(): Flow[Message, Message, NotUsed] = {
    val connectionId = new ConnectionId // user for this connection

    val (inputSink, messagesRefFuture) = setupInput(connectionId)

    def warnOverflow() = // LATER consider dropping the connection if this happens..
      logger.warn(s"overflow sending messages to client: $connectionId")

    // create an actor ref to accept and buffer messages sent to the client
    val (out, outRefFuture) =
      Source
        .actorRef[Message](3, OverflowStrategy.dropBuffer)
        .fixedBuffer(outputBufferSize, warnOverflow)
        .foreach {
          case BinaryMessage.Strict(data) => gaugeOutputSize(data.size)
          case TextMessage.Strict(data)   => gaugeOutputSize(data.size)
          case x                          => logger.warn(s"can't Gauge output size of $x")
        }
        .peekMat

    reportOnOpen(connectionId, messagesRefFuture, outRefFuture)
    Flow.fromSinkAndSource(inputSink, out)
  }

  /** Setup the input stream.
    *
    * @return a Sink for incoming web socket messages (for the WebSocket input flow)
    * and a future actor ref for GameCommand messages (which the caller can use to
    * inject GameCommand messages from outside the flow.)
    */
  private def setupInput(
        connectionId: ConnectionId
  )(implicit parentSpan: Span): (Sink[Message, NotUsed], Future[ActorRef]) = {

    // watch for closing the socket
    val (inputNotifyDone, terminationFutureMat) =
      Flow[Message].watchTermination()(Keep.right).peekMat

    def warnInputOverflow() = logger.warn(s"input buffer overflow on $connectionId")

    // buffer input messages from the client
    val inputBuffered: Flow[Message, Message, NotUsed] =
      inputNotifyDone
        .foreach { m =>
          logger.trace(s"received message on $connectionId. message: $m")
        }
        .foreach {
          case BinaryMessage.Strict(data) => gaugeInputSize(data.size)
          case TextMessage.Strict(data)   => gaugeInputSize(data.size)
          case x                          => logger.warn(s"can't Gauge input size of $x")
        }
        .fixedBuffer(inputBufferSize, warnInputOverflow)
        .mapMaterializedValue(_ => NotUsed)
        .named("inputBuffered")

    // convert web socket messages into client controller messages
    val inputConverted: Flow[Message, AppMessage, NotUsed] =
      inputBuffered
        .collect {
          case BinaryMessage.Strict(data) =>
            logger.trace(s"received data on $connectionId. data: $data")
            ClientMessage(connectionId, data)
        }
        .named("inputConverted")

    // target for sending connection open/close messages to the controller
    val (internalMessages, internalRefFuture) =
      Source
        .actorRef[AppMessage](internalMessagesSize, OverflowStrategy.fail)
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
      logger.info(s"reportOnOpen connection opened: $connectionId")
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
      terminationFuture.onComplete { fDone =>
        logger.info(s"reportOnClose closed: $connectionId  $fDone")
        messagesRef ! Gone(connectionId)
      }
      terminationFuture.failed.foreach { err =>
        logger.warn(s"reportOnClose $connectionId didn't close normally: $err")
      }
    }
  }

  private def gaugeInputSize(size: Int): Unit = {
    Gauged("inputBytes", size)
  }

  private def gaugeOutputSize(size: Int): Unit = {
    Gauged("outputBytes", size)
  }

}
