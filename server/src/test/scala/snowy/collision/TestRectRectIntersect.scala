package snowy.collision

import org.scalatest.PropSpec
import org.scalatest.prop.PropertyChecks
import snowy.playfield.Intersect._
import snowy.playfield.Rect

class TestRectRectIntersect extends PropSpec with PropertyChecks {
  val rectCases =
    Table(
      ("rectA", "rectB", "intersects"),
      (Rect(0, 0, 5, 5), Rect(3, 3, 6, 6), true), // intersect lower right
      (Rect(0, 0, 5, 5), Rect(6, 6, 7, 7), false), // miss right
      (Rect(0, 0, 5, 5), Rect(5, 0, 7, 2), true), // intersect right
      (Rect(1, 1, 2, 2), Rect(0, 0, 2, 2), true) // surround
    )
  property("rectangle collision tests") {
    forAll(rectCases) { (a, b, intersect) =>
      assert(a.intersectRect(b) == intersect)
    }
  }
}
