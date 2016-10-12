import GameCollide.{treeCollide, treeTrunk}
import snowy.GameConstants.Collision._
import snowy.GameConstants.maxSpeed
import snowy.playfield.{Circle, Sled, Tree}
import vector.Vec2d

object SledTree {
  /** Intersect the sled with all potentially overlapping trees on the playfield.
    *
    * @return a damaged sled if it overlaps with a tree */
  def collide(sled:Sled, trees:Set[Tree]): Option[Sled] = {
    val sledBody = Circle(sled.pos, sled.size / 2)

    trees.collectFirst {
      case tree if treeCollide(tree, sledBody) =>
        treeDamaged(sled, sledBody, tree)
    }
  }


  /** return a damaged version of the sled after impacting with a tree */
  private def treeDamaged(sled:Sled, sledBody:Circle, tree:Tree): Sled = {
    // take damage proportional to speed
    val health = {
      val speed = sled.speed.length
      if (speed <= safeSpeed) {
        sled.health
      } else {
        val damage = maxTreeCost * (speed / maxSpeed)
        math.max(sled.health - damage, treeMinHealth)
      }
    }

    // rebound slower downhill
    val rebound = {
      val leftRight = -math.signum(sled.speed.x)
      val x = math.max(math.abs(sled.speed.x) / 2, 10)
      Vec2d(leftRight * x, sled.speed.y / 5)
    }

    // force sled position to the edge of the tree
    val newPos = {
      val trunk = treeTrunk(tree)
      val edge = Collisions.rectClosestPerimeterPoint(trunk, sledBody)
      val edgeToSled = sled.pos - edge
      val edgeToSledLength = edgeToSled.length
      val result =
        if (edgeToSledLength < sledBody.radius) {
          val adjust = edgeToSled.unit * (sledBody.radius + treePadding - edgeToSledLength)
          sled.pos + adjust
        } else {
          sled.pos
        }
      result
    }

    sled.copy(health = health, speed = rebound, pos = newPos)
  }

}
