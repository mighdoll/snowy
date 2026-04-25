package snowy.util

import org.scalatest.propspec.AnyPropSpec
import snowy.util.RateLimit.rateLimit

import scala.concurrent.duration.*

class TestRateLimit extends AnyPropSpec {

  property("rate limit limits calls") {
    var count = 0
    val limited = rateLimit(10.milliseconds) {
      count = count + 1
    }
    (0 to 100).foreach(_ => limited())
    assert(count == 1)
  }
}
