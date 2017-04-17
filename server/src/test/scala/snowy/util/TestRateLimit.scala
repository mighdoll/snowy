package snowy.util

import org.scalatest.PropSpec
import org.scalatest.prop._
import RateLimit.rateLimit
import scala.concurrent.duration._

class TestRateLimit extends PropSpec with PropertyChecks {

  property("rate limit limits calls") {
    var count = 0
    val limited = rateLimit(10.milliseconds) {
      count = count + 1
    }
    (0 to 100).foreach(_ => limited())
    assert(count == 1)
  }

}
