import org.scalatest.PropSpec
import snowy.playfield.{Sled, Snowball}
import vector.Vec2d
import snowy.playfield.PlayId.{BallId, SledId}
import snowy.playfield.SnowballFixture.testball

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
