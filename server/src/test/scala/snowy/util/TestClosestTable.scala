package snowy.util

import org.scalatest.PropSpec
import org.scalatest.prop.TableDrivenPropertyChecks._

class TestClosestTable extends PropSpec {
  val table = new ClosestTable(2, 1, 2, 3)
  val examples =
    Table(
      ("index", "expected"),
      (0, 1),
      (1, 1),
      (2, 1),
      (3, 2),
      (4, 3),
      (5, 3)
    )

  property("below, above, and in range indexing works") {
    forAll(examples) {
      case (index, expected) =>
        assert(table.get(index) === expected)
    }
  }
}
