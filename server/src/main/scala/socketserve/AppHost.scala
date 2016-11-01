package socketserve

import scala.collection.mutable
import scala.concurrent.duration._
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.ws.{BinaryMessage, TextMessage}
import AppHost.Protocol._
import akka.util.ByteString

class AppHost(implicit system: ActorSystem) extends AppHostApi {

  private val connections = mutable.Map[ConnectionId, ActorRef]()
  private var app: Option[AppController] = None

  import system.dispatcher

  val appActor = system.actorOf(Props(new Actor {
    def receive: Receive = {
      case ClientMessage(id, text) => clientMessage(id, text)
      case Open(id, out)           => open(id, out)
      case Gone(id)                => gone(id)
      case RegisterApp(newApp)     => app = Some(newApp)
      case Turn                    => tickFn.map { fn => fn() }
    }
  }))

  private var tickFn: Option[() => Unit] = None

  def tick(duration: FiniteDuration)(fn: => Unit) {
    tickFn = Some(() => fn)
    system.scheduler.schedule(initialDelay = duration, interval = duration,
      receiver = appActor, message = Turn)
  }

  /** Send a message to one client */
  def send(msg: String, id: ConnectionId): Unit = {
    connections.get(id) match {
      case Some(out) =>
        out ! TextMessage(msg)
      case None      =>
        println(s"send to unknown connection id: $id")
    }
  }

  /** Send a binary message to one client */
  def sendBinary(data: ByteString, id: ConnectionId): Unit = {
    connections.get(id) match {
      case Some(out) =>
        out ! BinaryMessage(data)
      case None      =>
        println(s"send to unknown connection id: $id")
    }
  }

  /** Broadcast a message to all clients */
  def sendAll(msg: String): Unit = {
    for (out <- connections.values) {
      out ! TextMessage(msg)
    }
  }


  /** register a controller application for this host */
  def registerApp(multiApp: AppController): Unit = {
    appActor ! RegisterApp(multiApp)
  }

  private def clientMessage(id: ConnectionId, text: String): Unit = {
    app.map(_.message(id, text))
  }

  private def open(id: ConnectionId, out: ActorRef): Unit = {
    connections += id -> out
    app.map(_.open(id))
  }

  private def gone(id: ConnectionId): Unit = {
    connections -= id
    app.map(_.gone(id))
  }
}

object AppHost {

  object Protocol {

    sealed trait GameCommand

    case class Open(id: ConnectionId, out: ActorRef) extends GameCommand

    case class ClientMessage(id: ConnectionId, message: String) extends GameCommand

    case class Gone(id: ConnectionId) extends GameCommand

    case class RegisterApp(app: AppController) extends GameCommand

    case object Turn extends GameCommand

  }

}
