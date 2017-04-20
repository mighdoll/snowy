package snowy.playfield

import org.scalatest.PropSpec
import snowy.server.GameSeeding.randomTrees
import snowy.playfield.PlayfieldTracker.nullTreeTracker

class TestSeeding extends PropSpec {
  property("seeding works") {
    val trees = randomTrees()(nullTreeTracker)
    assert(trees.size > 100)
  }

}
