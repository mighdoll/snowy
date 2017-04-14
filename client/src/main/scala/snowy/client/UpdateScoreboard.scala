package snowy.client

import snowy.GameClientProtocol.Scoreboard
import org.scalajs.dom._
import snowy.connection.GameState
import snowy.playfield.Sled
object UpdateScoreboard {
  private val scoreTable =
    document.getElementById("scores").asInstanceOf[html.Div]
  val myScoreElement = document.getElementById("my-score").asInstanceOf[html.Div]
  val myScoreName    = myScoreElement.children(0)
  val mySledScore    = myScoreElement.children(1)

  def updateScoreboard(scoreboard: Scoreboard): Unit = {
    scoreboard.scores.sortWith(_.score > _.score).zipWithIndex.foreach {
      case (score, index) =>
        val scoreElement = scoreTable.children(index)
        val scoreName    = scoreElement.children(0)
        val sledScore    = scoreElement.children(1)

        scoreName.innerHTML = score.userName
        sledScore.innerHTML = math.floor(score.score).toString
    }
    myScoreName.innerHTML = GameState.serverMySled.getOrElse(Sled.dummy).userName
    mySledScore.innerHTML = math.floor(scoreboard.myScore).toString
  }
}
