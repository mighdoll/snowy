package snowy.collision

import snowy.GameConstants.treeSize
import snowy.collision.Collisions._
import snowy.playfield.{Circle, Rect, Snowball, Tree}
import vector.Vec2d

object GameCollide {
  val toTreeTopLeft = Vec2d(treeSize.x / 2, treeSize.y / 2)

  /** Intersect a snowball with all potentially overlapping trees on the playfield.
    *
    * @return true if the tree collides */
  def snowballTrees(snowball: Snowball, trees: Set[Tree]): Boolean = {
    val ball = Circle(snowball.position, snowball.radius)
    trees.find(treeCollide(_, ball)).isDefined
  }

  /** return true if the tree trunk intersects the sled body */
  def treeCollide(tree: Tree, body: Circle): Boolean = {
    val trunk = treeTrunk(tree)
    circleRectCollide(body, trunk)
  }

  def treeTrunk(tree: Tree): Rect = {
    val topLeft = tree.position - toTreeTopLeft
    Rect(topLeft, treeSize)
  }
}
