package socketserve

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.ws.{BinaryMessage, TextMessage}
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.util.ActorUtil.materializerWithLogging
import socketserve.AppHost.Protocol._
import snowy.util.FlowImplicits._

import scala.collection.mutable
import scala.concurrent.duration._

class AppHost(implicit system: ActorSystem) extends AppHostApi with Logging {
  private implicit val materializer      = materializerWithLogging(logger)
  private var app: Option[AppController] = None
  private val connections                = mutable.Map[ClientId, ActorRef]()
  private val tickTime
    : FiniteDuration                = 20 milliseconds // LATER get this from GameControl
  private val internalMessagesQueue = 10

  /** if we're behind on ticks, just skip one */
  private def tickBehind(a: AppMessage, b: AppMessage): AppMessage = {
    logger.warn("running slowly, skipping turn")
    a
  }

  private val tickSource =
    Source
      .tick[AppMessage](tickTime, tickTime, Tick)
      .conflate(tickBehind)
      .named("tickSource")

  private val (internalMessages, messagesRefFuture) =
    Source
      .actorRef[AppMessage](bufferSize = 2, OverflowStrategy.fail)
      .fixedBuffer(internalMessagesQueue, logger.warn("internal message overflow"))
      .named("internalMessages")
      .peekMat

  private val handlerSink = Flow[AppMessage]
    .to(Sink.foreach {
      case ClientMessage(id, binary) => clientMessage(id, binary)
      case Open(id, out)             => open(id, out)
      case Gone(id)                  => gone(id)
      case RegisterApp(newApp)       => app = Some(newApp)
      case Tick                      => app.foreach(_.tick())
    })
    .named("handlerSink")

  // LATER use mergePreferred ticks and internalMessages
  private val mergeSink = MergeHub
    .source[AppMessage]
    .merge(tickSource)
    .merge(internalMessages)
    .to(handlerSink)
    .run()

  def messagesSink(): Sink[AppMessage, NotUsed] = mergeSink

  import system.dispatcher

  /** Send a message to one client */
  def send(msg: String, id: ConnectionId): Unit = {
    connections.get(id) match {
      case Some(out) => out ! TextMessage(msg)
      case None      => logger.warn(s"send to unknown connection id: $id")
    }
  }

  /** Send a binary message to one client */
  def sendBinary(data: ByteString, id: ConnectionId): Unit = {
    connections.get(id) match {
      case Some(out) => out ! BinaryMessage(data)
      case None      => logger.warn(s"send to unknown connection id: $id")
    }
  }

  /** Broadcast a message to all clients */
  def sendAll(msg: String): Unit = {
    for (out <- connections.values) {
      out ! TextMessage(msg)
    }
  }

  /** register a controller application for this host
    * Called by the application setup to inject the app api that will
    * accept messages.
    */
  def registerApp(controller: AppController): Unit = {
    messagesRefFuture.foreach { messagesRef =>
      messagesRef ! RegisterApp(controller)
    }
  }

  private def clientMessage(id: ConnectionId, binary: ByteString): Unit = {
    app.foreach(_.message(id, binary))
  }

  private def open(id: ConnectionId, out: ActorRef): Unit = {
    connections += id -> out
    app.foreach(_.open(id))
  }

  private def gone(id: ConnectionId): Unit = {
    connections -= id
    app.foreach(_.gone(id))
  }

}

object AppHost {

  object Protocol {

    sealed trait AppMessage

    case class Open(id: ConnectionId, out: ActorRef) extends AppMessage

    case class ClientMessage(id: ConnectionId, message: ByteString) extends AppMessage

    case class Gone(id: ConnectionId) extends AppMessage

    case class RegisterApp(app: AppController) extends AppMessage

    case object Tick extends AppMessage

  }

}
