package snowy.playfield
import snowy.GameConstants.Bullet
import snowy.playfield.PlayId._
import vector.Vec2d

case class Snowball(id: PlayId[Snowball] = PlayfieldObject.nextId(),
                    var _position: Vec2d,
                    ownerId: SledId,
                    override var speed: Vec2d,
                    radius: Double,
                    spawned: Long,
                    var health: Double,
                    lifetime: Double,
                    override val impactDamage: Double)
    extends CircularObject {
  type MyType = Snowball

  override def canEqual(a: Any): Boolean = a.isInstanceOf[MyType]

  override def copyWithUpdatedPos(newPos: Vec2d): Snowball = this.copy(_position = newPos)

  /** a typical snowball is 1/10th the mass of a typical sled */
  override def mass: Double = (radius / Bullet.averageRadius) / 10

  override def armor: Double = 1

}
