package snowy.playfield

import org.scalatest.PropSpec
import snowy.playfield.PlayId.BallId
import snowy.playfield.SnowballFixture.testSnowball
import vector.Vec2d

class TestEqualsPlayfieldObject extends PropSpec {
  property("different sleds with same id are =") {
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullSledTracker
    val one = Sled("one")
    val two = new BasicSled(
      userName = "two"
    ) {
      override val id = one.id
    }
    assert(one.hashCode == two.hashCode)
    assert(one == two)
  }

  property("sled and snowball with same id are !=") {
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullSledTracker
    val one = Sled("one")
    val two = testSnowball(new BallId(one.id.id))
    assert(one != two)
  }

  property("sleds with the same id but different types are !=") {
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullSledTracker
    val one = new BasicSled("sled")
    val two = new TankSled("sled") {
      override val id = one.id
    }
    assert(one != two)
  }

}
