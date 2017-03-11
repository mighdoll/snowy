package snowy.client

import snowy.GameClientProtocol.Scoreboard
import org.scalajs.dom._
object UpdateScoreboard {
  val scoreboardDiv = document.getElementById("scoreboard").asInstanceOf[html.Div]
  def newScoreboard(scoreboard: Scoreboard): Unit ={
    //TODO: Use css classes instead of dom styling
    scoreboardDiv.innerHTML = ""
    scoreboardDiv.style.width = "150px"
    scoreboardDiv.style.height = "200px"
    scoreboard.scores.sortWith(_.score > _.score).map { score =>
      val scoreElement = document.createElement("div").asInstanceOf[html.Div]
      scoreElement.style.width = "150px"
      scoreElement.style.height = "20px"
      val scoreName =  document.createElement("div").asInstanceOf[html.Div]
      val sledScore = document.createElement("div").asInstanceOf[html.Div]

      scoreName.innerHTML = score.userName
      sledScore.innerHTML = math.floor(score.score).toString

      scoreName.style.cssFloat = "left"
      sledScore.style.cssFloat = "right"

      scoreElement.appendChild(scoreName)
      scoreElement.appendChild(sledScore)
      scoreboardDiv.appendChild(scoreElement)
    }
  }
}
