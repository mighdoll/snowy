package snowy.playfield

import org.scalacheck._
import org.scalatest._
import org.scalatest.prop._
import snowy.playfield.Intersect._
import vector.Vec2d

case class Ball(x: Double, y: Double)(implicit playfieldTracker: PlayfieldTracker[Ball])
    extends PlayfieldItem[Ball] {
  override var health = 1.0
  val radius                    = 5
  override def toString: String = s"Ball(${position.x}, ${position.y})"
  override def boundingBox =
    Rect(position - Vec2d(radius, radius), Vec2d(radius * 2, radius * 2))

  position_=(Vec2d(x, y))(Ball.nullTracker)
  playfieldTracker.add(this)
}

object Ball {
  def apply(pos: Vec2d)(implicit playfieldTracker: PlayfieldTracker[Ball]): Ball = {
    Ball(pos.x, pos.y)
  }
  val nullTracker = PlayfieldTracker.nullTracker[Ball]
}

class TestGrid extends PropSpec with PropertyChecks {
  val sizeX = 400
  val sizeY = 400
  val size  = Vec2d(sizeX, sizeY)

  val genGridXY: Gen[(Int, Int)] =
    for {
      x <- Gen.chooseNum[Int](-1, sizeX + 1, sizeX)
      y <- Gen.chooseNum[Int](-1, sizeY + 1, sizeY)
    } yield (x, y)

  val genPosition: Gen[Vec2d] =
    for {
      (x, y) <- genGridXY
    } yield Vec2d(x, y)

  val genPositions =
    for {
      count     <- Gen.choose(1, 500)
      positions <- Gen.listOfN(count, genPosition)
    } yield {
      positions
    }

  val genRect =
    for {
      (x, y) <- genGridXY
      height <- Gen.choose(5, 100)
      width  <- Gen.choose(5, 100)
    } yield Rect(Vec2d(x, y), Vec2d(width, height))

  property("adding and removing items from the grid") {
    forAll(genPositions) { positions =>
      implicit val grid = new Grid[Ball](size, 100)
      val balls         = positions.map(Ball(_))
      balls.foreach(grid.remove)
      assert(grid.items.size == 0)
    }
  }

  property("move one ball, inside still works") {
    implicit val grid = new Grid[Ball](size, 100)
    val ball          = Ball(5, 5)
    ball.position = Vec2d(110, 110)
    val found = grid.inside(Rect(100, 100, 150, 150))
    assert(found.headOption == Some(ball))
  }

  property("move one ball take 2, inside still works") {
    implicit val grid = new Grid[Ball](size, 100)
    val ball          = Ball(157, 1)
    ball.position = Vec2d(121, 399)
    val found = grid.inside(Rect(112, 367, 130, 450))
    assert(found.headOption == Some(ball))
  }

  property("inside calculation works on the grid") {
    forAll(genRect, genPositions, MinSuccessful(100)) { (rect, positions) =>
      insideTest(rect, positions)
    }
  }

  property("inside calculation specific cases") {
    val table =
      Table(("bounds", "positions"), (Rect(0, 73, 33, 97), Seq(Vec2d(1, 100))))

    forAll(table) { (rect, positions) =>
      insideTest(rect, positions)
    }
  }

  /** verify that grid.inside works on a set of positions */
  def insideTest(rect: Rect, positions: Seq[Vec2d]): Unit = {
    implicit val grid = new Grid[Ball](size, 100)
    val balls = positions.map { pos =>
      Ball(pos.x, pos.y)
    }
    val gridInside   = grid.inside(rect).toSet
    val actualInside = findInside(rect, balls).toSet
    assert(gridInside == actualInside)
  }

  /** @return all balls in a collection inside a rectangle */
  def findInside(rect: Rect, balls: Seq[Ball]): Seq[Ball] = {
    balls.filter { ball =>
      rect.intersectRect(ball.boundingBox)
    }
  }

}
