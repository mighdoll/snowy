package snowy.client

import snowy.GameClientProtocol.Scoreboard
import org.scalajs.dom._
object UpdateScoreboard {
  private val scoreTable =
    document.getElementById("scores").asInstanceOf[html.Div]
  def updateScoreboard(scoreboard: Scoreboard): Unit = {
    scoreboard.scores.sortWith(_.score > _.score).zipWithIndex.foreach { case (score, index) =>
      val scoreElement = scoreTable.children(index)
      val scoreName = scoreElement.children(0)
      val sledScore =  scoreElement.children(1)

      scoreName.innerHTML = score.userName
      sledScore.innerHTML = math.floor(score.score).toString
    }
  }
}
