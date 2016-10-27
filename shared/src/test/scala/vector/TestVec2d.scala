package vector

import org.scalatest._
import org.scalatest.prop._
import org.scalacheck.Gen._

class TestVec2d extends PropSpec with PropertyChecks {
  def angleZero(d: Double): Unit = {
    val a = Vec2d(d, d)
    val b = Vec2d(d, d)
    a.angle(b) === 0
  }

  property("angle between identical vectors is zero") {
    forAll(chooseNum(-1e100, 1e100)) {
      angleZero _
    }
  }

  property("unitUp.rotate is the same as fromRotation") {
    forAll(chooseNum(-1e100, 1e100)) {radians =>
      Vec2d.fromRotation(radians) === Vec2d.unitUp.rotate(radians)
    }
  }

}
