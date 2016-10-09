import org.scalatest._
import org.scalatest.prop._
import org.scalacheck._

class TestGameMotion extends PropSpec with PropertyChecks {

  def wrapInRange(value:Double):Unit = {
    val max = 100
    val wrapped = GameMotion.wrapBorder(value, max)
    assert(wrapped >= 0)
    assert(wrapped <= max)
  }

  val fixedValues = Table(
    ("value"),
    (10),
    (110),
    (210),
    (-110),
    (-210)
  )

  property("wrapBorder stays in range") {
    forAll(fixedValues) { wrapInRange(_) }
    forAll(Gen.choose(-1000.0, 1000.0)) { wrapInRange(_) }
  }

}
