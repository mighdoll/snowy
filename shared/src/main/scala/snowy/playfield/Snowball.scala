package snowy.playfield

import snowy.playfield.PlayId._
import vector.Vec2d

case class Snowball( ownerId: SledId,
                    override var speed: Vec2d,
                    radius: Double,
                    mass: Double,
                    spawned: Long,
                    var health: Double,
                    lifetime: Double,
                    override val impactDamage: Double)
    extends CircularObject[Snowball] {

  override def boundingBox = Rect(position, Vec2d(radius * 2, radius * 2))

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Snowball]
}
