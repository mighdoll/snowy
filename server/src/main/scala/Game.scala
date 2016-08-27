import java.util.concurrent.ThreadLocalRandom

import akka.actor._
import GameCommand._

import scala.collection.mutable
import upickle.default._
import GameClientProtocol._

/** messages to game actor */
object GameCommand {
  sealed trait GameCommand
  case class Open(id:ConnectionId, out:ActorRef) extends GameCommand
  case class ClientMessage(id:ConnectionId, message:String) extends GameCommand
  case class Gone(id:ConnectionId) extends GameCommand
  case object Turn extends GameCommand
}

class Game(implicit system:ActorSystem) extends GameControl {
  val ref = system.actorOf(Props(new Actor{
      def receive:Receive = {
        case ClientMessage(id, text) => clientMessage(id, text)
        case Open(id, out)           => join(id, out)
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
    out ! write(playField)
    updateClients() // TODO updateClients on a timer
  }

  private def updateClients(): Unit = {
    val state = currentState()
    for ((id, out) <- connections) {
      out ! state
    }
  }

  private def gone(id:ConnectionId):Unit = {
    println(s"gone received: $id")
    connections -= id
    sleds -= id
    snowballs -= id
  }

}

trait GameControl {
  val playField = PlayField(2000, 5000)
  val sleds = mutable.Map[ConnectionId, Sled]()
  val trees = randomTrees()
  val snowballs  = mutable.Map[ConnectionId, Snowball]()

  def randomTrees():mutable.Set[Tree] = {
    val num = playField.width * playField.height / 10000
    val set = new mutable.HashSet[Tree]()
    (0 to num).foreach{ i =>
      val x = i // TODO use random
      val y = i
      set += Tree(5, Position(x,y))
    }
    set
  }

  def currentState():State = {
    val justSleds = sleds.map{case (_, sled) => sled}.toSeq
    val justSnowballs = snowballs.map{case (_, snowball) => snowball}.toSeq
    State(justSleds, justSnowballs)
  }

}
