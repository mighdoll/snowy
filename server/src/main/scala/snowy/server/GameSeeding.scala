package snowy.server

import java.util.concurrent.ThreadLocalRandom
import scala.annotation.tailrec
import scala.collection.mutable
import snowy.GameConstants.playfield
import snowy.playfield.GameMotion.wrapInPlayfield
import snowy.playfield.{PlayfieldTracker, Tree}
import vector.Vec2d

/** Utilities for putting game objects in random places on the field */
object GameSeeding {

  /** Initialize a set of playfield obstacles */
  def randomTrees()(implicit tracker: PlayfieldTracker[Tree]): Set[Tree] = {
    val random = ThreadLocalRandom.current

    val clumpAmount = 80000 // average one clump in this many pixels
    val width       = playfield.x.toInt
    val height      = playfield.y.toInt
    val numClumps   = width * height / clumpAmount
    val inClump     = .75 // chance a tree is in an existing clump
    val clumpSize   = 200 // in the range pixels away

    val treeAmount = 200000
    val numTrees   = width * height / treeAmount

    val forest   = mutable.Buffer[mutable.Buffer[Tree]]()
    def treeSize = 20

    def nearbyTree(pos: Vec2d): Tree = {
      val x      = pos.x + random.nextInt(-clumpSize / 2, clumpSize / 2)
      val y      = pos.y + random.nextInt(-clumpSize / 2, clumpSize / 2)
      val newPos = wrapInPlayfield(Vec2d(x, y))
      Tree(newPos)
    }

    def nonOverlapping(fn: => Tree): Tree = {
      @tailrec
      def upToCount(attempts: Int): Tree = {
        val tree = fn
        if (!forest.flatten.forall(!treesOverlap(_, tree)) && attempts < 100) {
          upToCount(attempts + 1)
        } else {
          tree
        }
      }
      upToCount(0)
    }

    (0 to numClumps).foreach { _ =>
      if (random.nextDouble < inClump && forest.nonEmpty) {
        val clump = random.nextInt(forest.length)
        val near  = forest(clump)(random.nextInt(forest(clump).length)).position
        val tree  = nonOverlapping(nearbyTree(near))

        forest(clump) += tree
      } else {
        forest += mutable.Buffer(nonOverlapping {
          Tree(randomSpot())
        })
      }
    }
    (0 to numTrees).foreach { _ =>
      forest += mutable.Buffer(nonOverlapping {
        Tree(randomSpot())
      })
    }
    forest.flatten.toSet
  }

  /** pick a random spot on the playfield */
  def randomSpot(): Vec2d = {
    val random = ThreadLocalRandom.current
    Vec2d(
      random.nextInt(playfield.x.toInt),
      random.nextInt(playfield.y.toInt)
    )
  }

  /** @return true if the two trees overlap visually on the screen */
  private def treesOverlap(a: Tree, b: Tree): Boolean = {
    import math.abs
    val xDist = abs(a.position.x - b.position.x)
    val yDist = abs(a.position.y - b.position.y)
    xDist <= 50 && yDist <= 50
  }

}
