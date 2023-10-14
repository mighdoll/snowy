package snowy.playfield

import snowy.playfield.PlayId.*
import vector.Vec2d

case class Snowball(
      ownerId: SledId,
      var speed: Vec2d,
      radius: Double,
      mass: Double,
      spawned: Long,
      var health: Double,
      lifetime: Double,
      override val impactDamage: Double
) extends MovableCircularItem[Snowball] with SharedItem {

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Snowball]
}
