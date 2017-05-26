package snowy.server

import org.scalatest._
import org.scalatest.prop._
import snowy.server.ScoreLevels.levelForScore

class TestScoreLevels extends PropSpec with PropertyChecks {
  property("score 0 is level 1") {
    assert(levelForScore(0) === 1)
  }

  property("level increases monotonically with score") {
    val increment = 75
    val maxScore  = 100000

    (0 to maxScore by increment).foldLeft(1) {
      case (oldLevel, score) =>
        val level = levelForScore(score)
        assert(level >= oldLevel)
        level
    }
  }

}
