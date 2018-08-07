package snowy.playfield

import snowy.GameConstants
import vector.Vec2d

object Tree {
  def apply(initialPosition: Vec2d)(implicit tracker: PlayfieldTracker[Tree]): Tree = {
    new Tree().setInitialPosition(initialPosition)
  }
}

case class Tree() extends PlayfieldItem[Tree] {
  override def boundingBox = Rect(position, GameConstants.treeSize)

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Tree]

  override def impactDamage: Double = 1
}
