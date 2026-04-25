package vector

import org.scalacheck.Gen.*
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class TestVec2d extends AnyPropSpec with ScalaCheckPropertyChecks {
  def angleZero(d: Double): Unit = {
    val a = Vec2d(d, d)
    val b = Vec2d(d, d)
    assert(a.angle(b) === 0)
  }

  property("angle between identical vectors is zero") {
    forAll(chooseNum(-1e100, 1e100))(angleZero)
  }

  property("unitUp.rotate is the same as fromRotation") {
    forAll(chooseNum(-1e100, 1e100)) { radians =>
      assert(Vec2d.fromRotation(radians) === Vec2d.unitUp.rotate(radians))
    }
  }
}
