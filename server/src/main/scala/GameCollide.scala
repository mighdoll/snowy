import Collisions.circleRectCollide
import GameObjects.treeSize

object GameCollideHelper {
  val toTreeTopLeft = Vec2d(treeSize.x / 2, treeSize.y)

  /** return true if the tree trunk intersects the sled body */
  def treeCollide(tree: TreeState, body:Circle): Boolean = {
    val topLeft = tree.pos - toTreeTopLeft
    val treeTrunk = Rect(topLeft, treeSize)
    circleRectCollide(body, treeTrunk)
  }
}
import GameCollideHelper._

object GameCollide {
  /** Intersect a snowball with all potentially overlapping trees on the playfield.
    *
    * @return true if the tree collides */
  def snowballTrees(snowball:SnowballState, trees: Set[TreeState]): Boolean = {
    val ball = Circle(snowball.pos, snowball.size / 2)
    trees.find(treeCollide(_, ball)).isDefined
  }
}


