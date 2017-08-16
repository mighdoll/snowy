package snowy.server

import java.util.concurrent.ThreadLocalRandom
import scala.annotation.tailrec
import scala.collection.mutable
import com.typesafe.scalalogging.StrictLogging
import snowy.playfield.{Playfield, Tree}
import vector.Vec2d

/** Utilities for putting game objects in random places on the field */
class TreeSeeding(playfield: Playfield) extends StrictLogging {

  /** Initialize a set of playfield obstacles */
  def randomTrees(): Set[Tree] = {
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullTreeTracker
    val random = ThreadLocalRandom.current

    val clumpAmount = 80000 // average one clump in this many pixels
    val width       = playfield.size.x.toInt
    val height      = playfield.size.y.toInt
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
      val newPos = playfield.wrapInPlayfield(Vec2d(x, y))
      Tree(newPos)
    }

    def nonOverlapping(fn: => Tree): Tree = {
      val maxAttempts = 100
      @tailrec
      def upToCount(attempts: Int): Tree = {
        val tree = fn

        @inline def treeOverlaps: Boolean =
          !forest.flatten.forall(!treesOverlap(_, tree))

        if (treeOverlaps && attempts < maxAttempts) {
          upToCount(attempts + 1)
        } else {
          if (attempts >= maxAttempts)
            logger.warn(s"Tree seeding failed, attempts greater than $maxAttempts")

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
          Tree(playfield.randomSpot())
        })
      }
    }
    (0 to numTrees).foreach { _ =>
      forest += mutable.Buffer(nonOverlapping {
        Tree(playfield.randomSpot())
      })
    }
    forest.flatten.toSet
  }

  /** @return true if the two trees overlap visually on the screen */
  private def treesOverlap(a: Tree, b: Tree): Boolean = {
    import math.abs
    val xDist = abs(a.position.x - b.position.x)
    val yDist = abs(a.position.y - b.position.y)
    xDist <= 50 && yDist <= 50
  }

}
