package snowy.util

import org.scalatest.PropSpec
import org.scalatest.prop._

class TestEpochMicroseconds extends PropSpec with PropertyChecks {

  property("epoch microsecond clock aligns to millisecond clock") {
    val differences =
      (0 to 10000).map { _ =>
        val micros = EpochMicroseconds().value
        math.abs(System.currentTimeMillis() * 1000L - micros)
      }

    val probes =
      differences.sorted.reverse
        .drop(10) // drop the first few clock probes while our test warms up
    
    probes
      .foreach { d =>
        assert(d < 1000) // difference should be within a second
      }
  }

}
