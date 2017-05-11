package snowy.playfield

import org.scalacheck._
import org.scalatest._
import org.scalatest.prop._

class TestGameMotion extends PropSpec with PropertyChecks {

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
