package snowy.playfield

import snowy.playfield.PlayId.{BallId, SledId}
import vector.Vec2d

object SnowballFixture {

  def testSnowball(id: BallId = PlayId.nextId()) = {
    new Snowball(
      ownerId = new SledId(-2),
      speed = Vec2d.zero,
      radius = 20,
      mass = .1,
      spawned = 0,
      impactDamage = 1,
      lifetime = 1000,
      health = .3
    )
  }
}
