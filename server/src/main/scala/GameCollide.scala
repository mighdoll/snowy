import Collisions.circleRectCollide
import GameConstants.treeSize
import vector.Vec2d

object GameCollide {
  val toTreeTopLeft = Vec2d(treeSize.x / 2, treeSize.y)
  /** Intersect a snowball with all potentially overlapping trees on the playfield.
    *
    * @return true if the tree collides */
  def snowballTrees(snowball:Snowball, trees: Set[Tree]): Boolean = {
    val ball = Circle(snowball.pos, snowball.size / 2)
    trees.find(treeCollide(_, ball)).isDefined
  }

  /** return true if the tree trunk intersects the sled body */
  def treeCollide(tree: Tree, body:Circle): Boolean = {
    val trunk = treeTrunk(tree)
    circleRectCollide(body, trunk)
  }

  def treeTrunk(tree:Tree):Rect = {
    val topLeft = tree.pos - toTreeTopLeft
    Rect(topLeft, treeSize)
  }
}


