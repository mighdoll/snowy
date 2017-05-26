package snowy.server

/** Convert a user's score to an accomplishment level */
object ScoreLevels {

  private val linearScoreIncrement = 20000

  /** map of score -> level */
  private val levelUp = {
    val scores = Seq(100, 200, 500, 1000, 2000, 5000, 10000, linearScoreIncrement)
    scores.zipWithIndex.map {
      case (score, index) => score -> (index + 1)
    }
  }
  private val scoreLinearAtLevel: Int = levelUp.length

  def levelForScore(score: Double): Int = {
    val found =
      levelUp.collectFirst {
        case (levelScore, level) if levelScore > score => level
      }
    found.getOrElse {
      scoreLinearAtLevel + (score / linearScoreIncrement).toInt
    }
  }

}
