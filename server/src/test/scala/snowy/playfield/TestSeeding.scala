package snowy.playfield

import org.scalatest.PropSpec
import snowy.server.TreeSeeding
import vector.Vec2d

class TestSeeding extends PropSpec {
  property("seeding works") {
    val size = Vec2d(2800,4800)
    val seeding = new TreeSeeding(new Playfield(size))
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullTreeTracker
    val trees = seeding.randomTrees()
    assert(trees.size > 100)
  }

}
