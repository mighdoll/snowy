package snowy.collision

import snowy.GameConstants.Collision._
import snowy.GameConstants.absoluteMaxSpeed
import snowy.collision.GameCollide._
import snowy.playfield.GameMotion.wrapInPlayfield
import snowy.playfield.{Circle, PlayfieldTracker, Sled, Tree}
import vector.Vec2d

object SledTree {

  /** Intersect the sled with all potentially overlapping trees on the playfield.
    *
    * @return a damaged sled if it overlaps with a tree */
  def collide(sled: Sled, trees: Set[Tree])
             (implicit tracker:PlayfieldTracker[Sled]): Unit = {
    val sledBody = Circle(sled.position, sled.radius)

    trees.collectFirst {
      case tree if treeCollide(tree, sledBody) =>
        applyDamage(sled, sledBody, tree)
    }
  }

  /** modify a sled after impacting with a tree */
  private def applyDamage(sled: Sled, sledBody: Circle, tree: Tree)
                         (implicit tracker:PlayfieldTracker[Sled]): Unit = {
    // take damage proportional to speed
    val health = {
      val speed = sled.speed.length
      if (speed <= safeSpeed) {
        sled.health
      } else {
        val damage = maxTreeCost * (speed / absoluteMaxSpeed)
        math.max(sled.health - damage, treeMinHealth)
      }
    }

    // rebound slower downhill
    val rebound = {
      val leftRight = -math.signum(sled.speed.x)
      val x         = math.max(math.abs(sled.speed.x) / 2, 10)
      Vec2d(leftRight * x, sled.speed.y / 5)
    }

    // force sled position to the edge of the tree
    val newPosition = {
      val trunk            = treeTrunk(tree)
      val edge             = Collisions.rectClosestPerimeterPoint(trunk, sledBody)
      val edgeToSled       = sled.position- edge
      val edgeToSledLength = edgeToSled.length
      val result =
        if (edgeToSledLength < sledBody.radius) {
          val adjust = edgeToSled.unit * (sledBody.radius + treePadding - edgeToSledLength)
          wrapInPlayfield(sled.position + adjust)
        } else {
          sled.position
        }
      result
    }

    sled.health = health
    sled.speed = rebound
    sled.position = newPosition
  }

}
