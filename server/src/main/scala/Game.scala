import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import akka.actor._
import RandomUtil._
import GameCommand._
import scala.collection.mutable
import akka.http.scaladsl.model.ws.TextMessage

import GameClientMessages._

class Game(implicit system:ActorSystem) {
  val ref = system.actorOf(Props(new Actor{
      def receive:Receive = {
        case ClientMessage(user, text) => clientMessage(user, text)
        case Join(user, out)           => join(user, out)
        case Gone(user)                => gone(user)
      }
  }))

  val users = mutable.Map[User, ActorRef]()

  private def clientMessage(user:User, text:String):Unit = {
    println(s"message received: $user $text")
  }
  private def join(user:User, out:ActorRef):Unit = {
    println(s"join received: $user")
    users += user -> out
    val state = State(Seq(Snake(User("Milo"), Position(50,50))))
    broadcast(state.asJson.spaces2)
  }
  private def gone(user:User):Unit = {
    println(s"gone received: $user")
    users -= user
  }
  def broadcast(msg:String):Unit = {
    users.foreach { case (_, out) =>
      out ! TextMessage.Strict(msg)
    }
  }
}

object GameCommand {
  sealed trait GameCommand
  case class Join(user:User, out:ActorRef) extends GameCommand
  case class ClientMessage(user:User, message:String) extends GameCommand
  case class Gone(user:User) extends GameCommand
  case object Turn extends GameCommand
}

class User {
  val id = randomAlphaNum(8)
  override def toString = s"User_$id"
}
