package snowy.util

import org.scalatest.propspec.AnyPropSpec
import org.scalatest.prop.TableDrivenPropertyChecks.*

class TestClosestTable extends AnyPropSpec {
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
    forAll(examples) { case (index, expected) =>
      assert(table.get(index) === expected)
    }
  }
}
