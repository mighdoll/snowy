package snowy.playfield

import snowy.GameConstants.Bullet
import snowy.playfield.PlayId.SledId
import vector.Vec2d

object SnowballFixture {

  def testball() = new Snowball(
    _position = Vec2d.zero,
    ownerId = new SledId(-2),
    speed = Vec2d.zero,
    radius = 20,
    spawned = 0,
    impactDamage = 1,
    health =   .3
  )

}
