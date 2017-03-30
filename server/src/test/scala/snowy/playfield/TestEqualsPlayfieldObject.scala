package snowy.playfield

import org.scalatest.PropSpec
import snowy.playfield.PlayId.BallId
import snowy.playfield.SnowballFixture.testball
import vector.Vec2d

class TestEqualsPlayfieldObject extends PropSpec {
  property("different sleds with same id are =") {
    val one = Sled("one")
    val two = one.copy(id = one.id, speed = Vec2d.unitLeft)
    assert (one == two)
  }

  property("different sleds with same id have the same hash code") {
    val one = Sled("one")
    val two = one.copy(id = one.id, speed = Vec2d.unitLeft)
    assert (one.hashCode == two.hashCode)
  }

  property("sled and snowball with same id are !=") {
    val one = Sled("one")
    val two = testball().copy(id = new BallId(one.id.id))
    assert (one != two)
  }

}
