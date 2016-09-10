import java.util.concurrent.ThreadLocalRandom
import scala.collection.mutable
import GameClientProtocol._
import socketserve.ConnectionId
import Vec2dClientPosition._


/** Records the current state of sleds, trees, snowballs etc. */
trait GameState {
  self: GameControl =>

  val playField = PlayField(4000, 8000)
  var sleds = mutable.Map[ConnectionId, SledState]()
  val trees = randomTrees()
  val snowballs = mutable.ListBuffer[SnowballState]()
  val users = mutable.Map[ConnectionId, User]()
  var lastTime = System.currentTimeMillis()


  /** Package the relevant state to communicate to the client */
  protected def currentState(): Iterable[(ConnectionId, State)] = {
    def clientSled(id:ConnectionId):Sled = {
      val sledState = sleds(id)
      val userName = users.get(id).map(_.name).getOrElse("?")
      Sled(userName, sledState.pos.toPosition, sledState.rotation, sledState.turretRotation)
    }

    val clientSnowballs = snowballs.map { ball =>
      Snowball(ball.size.toInt, ball.pos.toPosition)
    }

    sleds.map { case (id, sledState) =>
      val mySled = sleds.keys.find(_ == id).map(clientSled(_)).get
      val otherSleds = sleds.keys.filter(_ != id).map(clientSled(_)).toSeq
      id -> State(mySled, otherSleds, clientSnowballs)
    }.toSeq
  }

  /** Initialize a set of playfield obstacles */
  private def randomTrees(): Set[TreeState] = {
    val sparsity = 170000 // average one tree in this many pixels
    val num = playField.width * playField.height / sparsity
    (0 to num).map { i =>
      TreeState(randomSpot(), 20)
    }.toSet
  }

  /** pick a random spot on the playfield */
  protected def randomSpot(): Vec2d = {
    val random = ThreadLocalRandom.current
    Vec2d(
      random.nextInt(playField.width),
      random.nextInt(playField.height)
    )
  }
}
