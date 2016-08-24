import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import akka.actor._
import GameCommand._
import scala.collection.mutable
import akka.http.scaladsl.model.ws.TextMessage

import GameClientMessages._

class Game(implicit system:ActorSystem) {
  val ref = system.actorOf(Props(new Actor{
      def receive:Receive = {
        case ClientMessage(id, text) => clientMessage(id, text)
        case Join(id, out)           => join(id, out)
        case Gone(id)                => gone(id)
      }
  }))

  val connections = mutable.Map[ConnectionId, ActorRef]()

  private def clientMessage(id:ConnectionId, text:String):Unit = {
    println(s"message received: $id $text")
  }

  private def join(id:ConnectionId, out:ActorRef):Unit = {
    println(s"join received: $id")
    connections += id -> out
    val state = State(Seq(Snake(User("Milo"), Position(50,50))))
    broadcast(state.asJson.spaces2)
  }

  private def gone(id:ConnectionId):Unit = {
    println(s"gone received: $id")
    connections -= id
  }

  def broadcast(msg:String):Unit = {
    connections.foreach { case (_, out) =>
      out ! TextMessage.Strict(msg)
    }
  }
}

object GameCommand {
  sealed trait GameCommand
  case class Join(id:ConnectionId, out:ActorRef) extends GameCommand
  case class ClientMessage(id:ConnectionId, message:String) extends GameCommand
  case class Gone(id:ConnectionId) extends GameCommand
  case object Turn extends GameCommand
}

object ConnectionId {
  import java.util.concurrent.atomic.AtomicLong
  val nextId = new AtomicLong
}
class ConnectionId {
  val id = ConnectionId.nextId.getAndIncrement()
  override def toString = s"Connection_$id"
}
