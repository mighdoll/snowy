package snowy.playfield

import snowy.PlayIdTag.given
import snowy.{playIdRW, GameConstants}
import upickle.default.ReadWriter
import vector.Vec2d

object Tree {
  def apply(initialPosition: Vec2d)(implicit tracker: PlayfieldTracker[Tree]): Tree = {
    val tree = new Tree(PlayId.nextId[Tree](), initialPosition)
    tree.setInitialPosition(initialPosition)
    tree
  }
}

case class Tree(override val id: PlayId[Tree], var internalPosition: Vec2d)
    extends PlayfieldItem[Tree] derives ReadWriter {
  override def boundingBox = Rect(position, GameConstants.treeSize)

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Tree]

  override def impactDamage: Double = 1
}
