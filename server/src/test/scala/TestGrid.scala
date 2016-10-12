import org.scalatest._
import org.scalatest.prop._
import GameConstants.playfield
import org.scalacheck._

class TestGrid extends PropSpec with PropertyChecks {
  val playfieldX = Gen.choose(0.0, playfield.x)
  val playfieldY = Gen.choose(0.0, playfield.y)

  property("add/remove an element anywhere on the grid") {
    val spacing = 100.0
    val grid = new Grid[SnowballState](playfield, spacing)
    val ownerId = PlayfieldObject.nextId()
    forAll(playfieldX, playfieldY, minSuccessful(200)) { (x, y) =>
      val ball = SnowballState(PlayfieldObject.nextId(), ownerId, Vec2d(x, y), 0, Vec2d.zero, 0)
      grid.add(ball)
      grid.remove(ball)
      grid.itemCount === 0
    }
  }
}
