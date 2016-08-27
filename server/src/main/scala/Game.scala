import java.util.concurrent.ThreadLocalRandom
import akka.actor._
import GameCommand._
import scala.concurrent.duration._
import scala.collection.mutable
import upickle.default._
import GameClientProtocol._
import scala.concurrent.duration.Duration
import akka.http.scaladsl.model.ws.TextMessage

/** messages to game actor */
object GameCommand {
  sealed trait GameCommand
  case class Open(id:ConnectionId, out:ActorRef) extends GameCommand
  case class ClientMessage(id:ConnectionId, message:String) extends GameCommand
  case class Gone(id:ConnectionId) extends GameCommand
  case object Turn extends GameCommand
}

class Game(implicit system:ActorSystem) extends GameControl {
  import system.dispatcher
  val gameActorRef = system.actorOf(Props(new Actor {
    def receive: Receive = {
      case ClientMessage(id, text) => clientMessage(id, text)
      case Open(id, out)           => join(id, out)
      case Gone(id)                => gone(id)
      case Turn                    => updateClients()
    }
  }))

  system.scheduler.schedule(initialDelay = 50 milliseconds, interval = 50 milliseconds,
    receiver = gameActorRef, message = Turn)

  val connections = mutable.Map[ConnectionId, ActorRef]()

  private def clientMessage(id:ConnectionId, text:String):Unit = {
    println(s"message received: $id $text")
  }

  private def join(id:ConnectionId, out:ActorRef):Unit = {
    println(s"join received: $id")
    connections += id -> out
    send(write(playField), out)
  }

  private def updateClients(): Unit = {
    val state:String = write(currentState())
    for ((id, out) <- connections) {
      send(state, out)
    }
  }

  private def send(msg:String, out:ActorRef):Unit = {
    out ! TextMessage.Strict(msg)
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

  def randomTrees():Set[Tree] = {
    val random = ThreadLocalRandom.current
    val num = playField.width * playField.height / 10000
    (0 to num).map{ i =>
      val x = random.nextInt(playField.width)
      val y = random.nextInt(playField.height)
      Tree(5, Position(x,y))
    }.toSet
  }

  def currentState():State = {
    val justSleds = sleds.map{case (_, sled) => sled}.toSeq
    val justSnowballs = snowballs.map{case (_, snowball) => snowball}.toSeq
    State(justSleds, justSnowballs)
  }

}
