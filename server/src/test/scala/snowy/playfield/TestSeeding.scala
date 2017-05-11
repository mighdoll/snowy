package snowy.playfield

import org.scalatest.PropSpec
import snowy.playfield.PlayfieldTracker.nullTreeTracker
import snowy.server.GameSeeding
import vector.Vec2d

class TestSeeding extends PropSpec {
  property("seeding works") {
    val size = Vec2d(2800,4800)
    val seeding = new GameSeeding(new Playfield(size))
    val trees = seeding.randomTrees()(nullTreeTracker)
    assert(trees.size > 100)
  }

}
