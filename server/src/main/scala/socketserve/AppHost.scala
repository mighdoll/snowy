package socketserve

import scala.collection.mutable
import scala.concurrent.duration._
import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.ws.{BinaryMessage, TextMessage}
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString
import com.typesafe.scalalogging.StrictLogging
import snowy.playfield.GameMotion.Turn
import socketserve.ActorUtil.materializerWithLogging
import socketserve.AppHost.Protocol._
import socketserve.FlowImplicits._

class AppHost(implicit system: ActorSystem) extends AppHostApi with StrictLogging {
  private implicit val materializer      = materializerWithLogging(logger)
  private var app: Option[AppController] = None
  private val connections                = mutable.Map[ClientId, ActorRef]()
  private val tickTime
    : FiniteDuration                = 20 milliseconds // LATER get this from GameControl
  private val internalMessagesQueue = 10

  /** if we're behind on ticks, just skip one */
  private def tickBehind(a: GameCommand, b: GameCommand): GameCommand = {
    logger.warn("running slowly, skipping turn")
    a
  }

  private val tickSource =
    Source
      .tick[GameCommand](tickTime, tickTime, Tick)
      .conflate(tickBehind)
      .named("tickSource")

  private val (internalMessages, messagesRefFuture) =
    Source
      .actorRef[GameCommand](bufferSize = 2, OverflowStrategy.fail)
      .fixedBuffer(internalMessagesQueue, logger.warn("internal message overflow"))
      .named("internalMessages")
      .peekMat

  private val handlerSink = Flow[GameCommand]
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
    .source[GameCommand]
    .merge(tickSource)
    .merge(internalMessages)
    .to(handlerSink)
    .run()

  def messagesSink(): Sink[GameCommand, NotUsed] = mergeSink

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

    sealed trait GameCommand

    case class Open(id: ConnectionId, out: ActorRef) extends GameCommand

    case class ClientMessage(id: ConnectionId, message: ByteString) extends GameCommand

    case class Gone(id: ConnectionId) extends GameCommand

    case class RegisterApp(app: AppController) extends GameCommand

    case object Tick extends GameCommand

  }

}
