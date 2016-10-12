import GameConstants.Collision._
import GameConstants.maxSpeed
import socketserve.ConnectionId
import GameCollide._

/** Collide a sled with trees and snowballs via the snowball() and tree() routines. */
class SledCollide(id:ConnectionId, sled: SledState, snowballs: Store[SnowballState], trees: Set[TreeState]) {
  val sledBody = Circle(sled.pos, sled.size / 2)

  /** Intersect the sled with all potentially overlapping snowballs on the playfield.
    * If a snowball collides with the sled, remove the snowball from the playfield
    *
    * @return a damaged sled if it intersects with a snowball */
  def snowball(): Option[(SledState, Store[SnowballState])] = {
    val collisions =
      snowballs.items.filter{snowball =>
        snowball.ownerId != sled.id && snowballCollide(snowball)
      }

    val newBalls = collisions.foldLeft(snowballs){(balls, snowball) =>
      balls.remove(snowball)
    }

    collisions.headOption.map { snowball =>
      (snowballDamaged(snowball), newBalls)
    }
  }

  /** Intersect the sled with all potentially overlapping trees on the playfield.
    *
    * @return a damaged sled if it overlaps with a tree */
  def tree(): Option[SledState] = {
    trees.collectFirst {
      case tree if treeCollide(tree, sledBody) =>
        treeDamaged(tree)
    }
  }

  /** return true if the snowball intersects the sled body */
  private def snowballCollide(snowball: SnowballState): Boolean = {
    val centerDistance = (sled.pos - snowball.pos).length
    val radiiSum = sled.size / 2 + snowball.size / 2
    centerDistance < radiiSum
  }

  /** return a damaged version of the sled after impacting with a tree */
  private def treeDamaged(tree:TreeState): SledState = {
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

  /** return a damaged version of the sled after impacting with a snowball */
  private def snowballDamaged(snowball: SnowballState): SledState = {
    val health = math.max(sled.health - snowballCost, 0)
    val stopped = sled.speed + snowball.speed * 30
    sled.copy(health = health, speed = stopped)
  }

}
