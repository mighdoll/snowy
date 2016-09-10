import java.util.concurrent.ThreadLocalRandom
import scala.annotation.tailrec
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
    def clientSled(id: ConnectionId): Sled = {
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

  /** @return true if tthe two trees overlap visually on the screen */
  private def treesOverlap(a: TreeState, b: TreeState): Boolean = {
    import math.abs
    val xDist = abs(a.pos.x - b.pos.x)
    val yDist = abs(a.pos.y - b.pos.y)
    xDist <= 50 && yDist <= 50
  }

  /** Initialize a set of playfield obstacles */
  private def randomTrees(): Set[TreeState] = {
    val random = ThreadLocalRandom.current
    val sparsity = 80000 // average one tree in this many pixels
    val num = playField.width * playField.height / sparsity
    val inClump = .75 // chance a tree is in an existing clump
    val clumpSize = 400 // in the range pixels away
    val forest = mutable.Buffer[TreeState]()
    def treeSize = 20

    def nearbyTree(pos: Vec2d): TreeState = {
      val x = pos.x + random.nextInt(-clumpSize / 2, clumpSize / 2)
      val y = pos.y + random.nextInt(-clumpSize / 2, clumpSize / 2)
      val newPos = wrapInPlayfield(Vec2d(x, y))
      TreeState(newPos, treeSize)
    }

    @tailrec
    def nonOverlapping(fn: => TreeState): TreeState = {
      val tree = fn
      forest.find(treesOverlap(_, tree)) match {
        case Some(t) => nonOverlapping(fn)
        case None    => tree
      }
    }

    def nonOverlapping2(fn: () => TreeState): TreeState = {
      var tree = fn()
      while (forest.find { t => treesOverlap(t, tree) }.isDefined) {
        tree = fn()
      }
      tree
    }

    (0 to num).foreach { _ =>
      if (random.nextDouble < inClump && !forest.isEmpty) {
        val near = forest(random.nextInt(forest.length)).pos
        val tree = nonOverlapping { nearbyTree(near) }
        forest += tree
      } else {
        forest += nonOverlapping { TreeState(randomSpot(), 20) }
      }
    }
    forest.toSet
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
