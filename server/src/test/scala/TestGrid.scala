import org.scalacheck._
import org.scalatest._
import org.scalatest.prop._
import snowy.GameConstants.playfield
import snowy.playfield.{Grid, PlayfieldObject, Sled, Snowball}
import vector.Vec2d

class TestGrid extends PropSpec with PropertyChecks {
  val playfieldX = Gen.choose(0.0, playfield.x)
  val playfieldY = Gen.choose(0.0, playfield.y)

  property("add/remove an element anywhere on the grid") {
    val spacing = 100.0
    val grid    = new Grid[Snowball](playfield, spacing)
    val ownerId = PlayfieldObject.nextId[Sled]()
    forAll(playfieldX, playfieldY, minSuccessful(200)) { (x, y) =>
      val ball =
        Snowball(PlayfieldObject.nextId(), Vec2d(x, y), ownerId, Vec2d.zero, 10, .1, 0, 1, 10, 1)
      grid.add(ball)
      grid.remove(ball)
      assert(grid.itemCount === 0)
    }
  }
}
