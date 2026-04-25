package snowy.playfield

import org.scalacheck.*
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class TestGameMotion extends AnyPropSpec with ScalaCheckPropertyChecks {

  val fixedValues = Table(
    ("value"),
    (10),
    (110),
    (210),
    (-110),
    (-210)
  )

  def wrapInRange(value: Double): Unit = {
    val max     = 100
    val wrapped = Playfield.wrapBorder(value, max)
    assert(wrapped >= 0)
    assert(wrapped <= max)
  }

  property("wrapBorder stays in range") {
    forAll(fixedValues) { wrapInRange(_) }
    forAll(Gen.choose(-1000.0, 1000.0)) { wrapInRange(_) }
  }

}
