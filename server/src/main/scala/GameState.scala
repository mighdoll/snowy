import java.util.concurrent.ThreadLocalRandom

import GameClientProtocol._
import socketserve.ConnectionId

import scala.annotation.tailrec
import scala.collection.mutable


/** Records the current state of sleds, trees, snowballs etc. */
trait GameState {
  self: GameControl =>

  val playField = PlayField(4000, 8000)
  val gridSpacing = 100.0
  val playfieldSize = Vec2d(playField.width, playField.height)
  val sleds = new PlayfieldStore[SledState](playfieldSize, gridSpacing)
  var snowballs = new PlayfieldMultiMap[SnowballState](playfieldSize, gridSpacing)
  val trees: Set[TreeState] = randomTrees()
  val users = mutable.Map[ConnectionId, User]()
  var lastTime = System.currentTimeMillis()
  val commands = new PendingCommands

  /** Package the relevant state to communicate to the client */
  protected def currentState(): Iterable[(ConnectionId, State)] = {
    val clientSnowballs = snowballs.map{(_, ball) => ball}.toSeq

    sleds.map { (myId, mySled) =>
      val otherSleds = sleds.collect {
        case (otherId, sled) if otherId != myId =>
          sled
      }.toSeq
      myId -> State(mySled, otherSleds, clientSnowballs)
    }.toSeq
  }

  /** @return true if the two trees overlap visually on the screen */
  private def treesOverlap(a: TreeState, b: TreeState): Boolean = {
    import math.abs
    val xDist = abs(a.pos.x - b.pos.x)
    val yDist = abs(a.pos.y - b.pos.y)
    xDist <= 50 && yDist <= 50
  }

  /** Initialize a set of playfield obstacles */
  private def randomTrees(): Set[TreeState] = {
    val random = ThreadLocalRandom.current

    val clumpAmount = 80000 // average one clump in this many pixels
    val numClumps = playField.width * playField.height / clumpAmount
    val inClump = .75 // chance a tree is in an existing clump
    val clumpSize = 200 // in the range pixels away

    val treeAmount = 200000
    val numTrees = playField.width * playField.height / treeAmount

    val forest = mutable.Buffer[mutable.Buffer[TreeState]]()
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
      forest.flatten.find(treesOverlap(_, tree)) match {
        case Some(t) => nonOverlapping(fn)
        case None    => tree
      }
    }

    (0 to numClumps).foreach { _ =>
      if (random.nextDouble < inClump && forest.nonEmpty) {
        val clump = random.nextInt(forest.length)
        val near = forest(clump)(random.nextInt(forest(clump).length)).pos
        val tree = nonOverlapping(nearbyTree(near))

        forest(clump) += tree
      } else {
        forest += mutable.Buffer(nonOverlapping {
          TreeState(randomSpot(), 20)
        })
      }
    }
    (0 to numTrees).foreach { _ =>
      forest += mutable.Buffer(nonOverlapping {
        TreeState(randomSpot(), 20)
      })
    }
    forest.flatten.toSet
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
