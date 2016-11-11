import org.scalatest.PropSpec
import snowy.server.GameSeeding.randomTrees

class TestSeeding extends PropSpec {
  property("seeding works") {
    val trees = randomTrees()
    assert(trees.size > 100)
  }

}
