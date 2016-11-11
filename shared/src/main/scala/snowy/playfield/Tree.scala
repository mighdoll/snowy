package snowy.playfield

import snowy.playfield.PlayId.TreeId
import vector.Vec2d

object Tree {
  def apply(initialPosition: Vec2d): Tree = new Tree(_position = initialPosition)
}

case class Tree(id: TreeId = PlayfieldObject.nextId(),
                var _position: Vec2d,
                var health: Double = 100,
                size: Double = 20)
    extends PlayfieldObject {
  type MyType = Tree

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Tree]

  override def copyWithUpdatedPos(newPos: Vec2d): Tree = this.copy(_position = newPos)

  override def armor: Double        = 1
  override def impactDamage: Double = 1

}
