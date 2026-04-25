package snowy.playfield

import org.scalatest.propspec.AnyPropSpec
import snowy.playfield.PlayId.BallId
import snowy.playfield.SnowballFixture.testSnowball

class TestEqualsPlayfieldObject extends AnyPropSpec {
  property("different sleds with same id are =") {
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullSledTracker
    val one = Sled("one")
    val two = new BasicSled(
      id = one.id,
      userName = "two",
      internalPosition = one.internalPosition
    )
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
    import vector.Vec2d
    val one = Sled("sled")
    val two = new TankSled(
      id = one.id,
      userName = "sled",
      internalPosition = Vec2d.zero
    )
    assert(one != two)
  }

}
