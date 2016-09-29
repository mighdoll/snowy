import scala.collection.mutable
import Collisions.circleRectCollide
import GameConstants.{snowballCollisionCost, treeCollisionCost}
import GameObjects.treeSize

/** Collide a sled with trees and snowballs via the snowball() and tree() routines. */
class SledCollide(sled: SledState, snowballs: mutable.ListBuffer[SnowballState], trees: Set[TreeState]) {
  val sledBody = Circle(sled.pos, sled.size / 2)
  private val toTreeTopLeft = Vec2d(treeSize.x / 2, treeSize.y)

  /** Intersect the sled with all potentially overlapping snowballs on the playfield.
    * If a snowball collides with the sled, remove the snowball from the playfield
    *
    * @return a damaged sled if it intersects with a snowball */
  def snowball(): Option[SledState] = {
    snowballs.collectFirst {
      case snowball if snowballCollide(snowball) =>
        snowballs.remove(snowballs.indexOf(snowball))
        snowballDamaged()
    }
  }

  /** Intersect the sled with all potentially overlapping trees on the playfield.
    *
    * @return a damaged sled if it overlaps with a tree */
  def tree(): Option[SledState] = {
    val sledBody = Circle(sled.pos, sled.size / 2)
    trees.collectFirst {
      case tree if treeCollide(tree) =>
        treeDamaged()
    }
  }

  /** return true if the snowball intersects the sled body */
  private def snowballCollide(snowball: SnowballState): Boolean = {
    val centerDistance = (sled.pos - snowball.pos).length
    val radiiSum = sled.size / 2 + snowball.size / 2
    centerDistance < radiiSum
  }

  /** return true if the tree trunk intersects the sled body */
  private def treeCollide(tree: TreeState): Boolean = {
    val topLeft = tree.pos - toTreeTopLeft
    val treeTrunk = Rect(topLeft, treeSize)
    circleRectCollide(sledBody, treeTrunk)
  }

  /** return a damaged version of the sled after impacting with a tree */
  private def treeDamaged(): SledState = {
    val health = math.max(sled.health - treeCollisionCost, 0)
    val stopped = sled.speed * -1
    sled.copy(health = health, speed = stopped)
  }

  /** return a damaged version of the sled after impacting with a snowball */
  private def snowballDamaged(): SledState = {
    val health = math.max(sled.health - snowballCollisionCost, 0)
    val stopped = sled.speed * -1
    sled.copy(health = health, speed = stopped)
  }

}
