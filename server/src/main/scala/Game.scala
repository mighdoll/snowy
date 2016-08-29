import akka.actor._
import GameCommand._
import scala.concurrent.duration._
import scala.collection.mutable
import GameClientProtocol._

import akka.http.scaladsl.model.ws.TextMessage

/** messages to game actor */
object GameCommand {
  sealed trait GameCommand
  case class Open(id:ConnectionId, out:ActorRef) extends GameCommand
  case class ClientMessage(id:ConnectionId, message:String) extends GameCommand
  case class Gone(id:ConnectionId) extends GameCommand
  case object Turn extends GameCommand
}

class Game(implicit system:ActorSystem) {
  val connections = mutable.Map[ConnectionId, ActorRef]()
  val game = new GameControl {
    override def send(msg:String, id:ConnectionId):Unit = {
      sendMessage(msg, connections(id))
    }
    override def sendAll(msg:String):Unit = {
      for (out <- connections.values) {
        sendMessage(msg, out)
      }
    }
  }

  import system.dispatcher
  val gameActorRef = system.actorOf(Props(new Actor {
    def receive: Receive = {
      case ClientMessage(id, text) => clientMessage(id, text)
      case Open(id, out)           => join(id, out)
      case Gone(id)                => gone(id)
      case Turn                    => game.tick()
    }
  }))

  system.scheduler.schedule(initialDelay = 50 milliseconds, interval = 50 milliseconds,
    receiver = gameActorRef, message = Turn)


  private def clientMessage(id:ConnectionId, text:String):Unit = {
    println(s"message received: $id $text")
    game.message(id, text)
  }

  private def join(id:ConnectionId, out:ActorRef):Unit = {
    println(s"join received: $id")
    connections += id -> out
    game.join(id)
  }

  private def sendMessage(msg:String, out:ActorRef):Unit = {
    out ! TextMessage.Strict(msg)
  }

  private def gone(id:ConnectionId):Unit = {
    println(s"gone received: $id")
    connections -= id
    game.gone(id)
  }
}

/** support for simple apps that support multiple users over websockets */
trait MultiConnect {
  // provided by server container

  def send(msg:String, id:ConnectionId):Unit
  def sendAll(msg:String):Unit

  // provided by subclass

  def join(id:ConnectionId):Unit
  def gone(id:ConnectionId):Unit
  def message(id:ConnectionId, msg:String):Unit
  def tick():Unit
}


