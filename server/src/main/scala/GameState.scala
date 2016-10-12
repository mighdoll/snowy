import java.util.concurrent.ThreadLocalRandom

import GameClientProtocol._
import socketserve.ConnectionId

import scala.annotation.tailrec
import scala.collection.mutable
import GameConstants.playfield


/** Records the current state of sleds, trees, snowballs etc. */
trait GameState {
  self: GameControl =>

  val gridSpacing = 100.0
  var sleds = Store[Sled](Set(), Grid(playfield, gridSpacing, Set()))
  var snowballs = Store[Snowball](Set(), Grid(playfield, gridSpacing, Set()))
  val trees: Set[Tree] = randomTrees()
  val users = mutable.Map[ConnectionId, User]()
  val sledMap = mutable.Map[ConnectionId, PlayId[Sled]]()
  var lastTime = System.currentTimeMillis()
  val commands = new PendingCommands

  /** Package the relevant state to communicate to the client */
  protected def currentState(): Iterable[(ConnectionId, State)] = {
    val clientSnowballs = snowballs.items.toSeq

    sleds.items.map { mySled =>
      val otherSleds = sleds.items.filter(_.id != mySled.id).toSeq
      mySled.connectionId -> State(mySled, otherSleds, clientSnowballs)
    }.toSeq
  }

  /** @return true if the two trees overlap visually on the screen */
  private def treesOverlap(a: Tree, b: Tree): Boolean = {
    import math.abs
    val xDist = abs(a.pos.x - b.pos.x)
    val yDist = abs(a.pos.y - b.pos.y)
    xDist <= 50 && yDist <= 50
  }

  /** Initialize a set of playfield obstacles */
  private def randomTrees(): Set[Tree] = {
    val random = ThreadLocalRandom.current

    val clumpAmount = 80000 // average one clump in this many pixels
    val width = playfield.x.toInt
    val height = playfield.y.toInt
    val numClumps = width * height / clumpAmount
    val inClump = .75 // chance a tree is in an existing clump
    val clumpSize = 200 // in the range pixels away

    val treeAmount = 200000
    val numTrees = width * height / treeAmount

    val forest = mutable.Buffer[mutable.Buffer[Tree]]()
    def treeSize = 20

    def nearbyTree(pos: Vec2d): Tree = {
      val x = pos.x + random.nextInt(-clumpSize / 2, clumpSize / 2)
      val y = pos.y + random.nextInt(-clumpSize / 2, clumpSize / 2)
      val newPos = wrapInPlayfield(Vec2d(x, y))
      Tree(PlayfieldObject.nextId(), newPos, treeSize)
    }

    @tailrec
    def nonOverlapping(fn: => Tree): Tree = {
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
          Tree(PlayfieldObject.nextId(), randomSpot(), 20)
        })
      }
    }
    (0 to numTrees).foreach { _ =>
      forest += mutable.Buffer(nonOverlapping {
        Tree(PlayfieldObject.nextId(), randomSpot(), 20)
      })
    }
    forest.flatten.toSet
  }

  /** pick a random spot on the playfield */
  protected def randomSpot(): Vec2d = {
    val random = ThreadLocalRandom.current
    Vec2d(
      random.nextInt(playfield.x.toInt),
      random.nextInt(playfield.y.toInt)
    )
  }

  implicit class SledIdOps(id: PlayId[Sled]) {
    def sled: Sled = {
      sleds.items.find(_.id == id).get
    }
  }

  implicit class SledIndices(sled: Sled) {
    def connectionId: ConnectionId = {
      sledMap.collectFirst {
        case (connectionId, sledId) if sled.id == sledId =>
          connectionId
      }.get
    }

    def remove(): Unit = {
      sledMap.collectFirst {
        case (connectionId, sledId) if sled.id == sledId =>
          sledMap.remove(connectionId)
          sleds = sleds.remove(sled)
      }
    }
  }
}
