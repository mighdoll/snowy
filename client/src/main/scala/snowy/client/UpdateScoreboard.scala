package snowy.client

import snowy.GameClientProtocol.Scoreboard
import org.scalajs.dom._
object UpdateScoreboard {
  // TODO make private?
  val scoreboardDiv = document.getElementById("scoreboard").asInstanceOf[html.Div]
  // TODO rename? 'new' sounds like you'd only create a scoreboard once per game..
  def newScoreboard(scoreboard: Scoreboard): Unit ={
    //TODO: Use css classes instead of dom styling
    scoreboardDiv.innerHTML = ""
    scoreboard.scores.sortWith(_.score > _.score).map { score =>
      val scoreElement = document.createElement("div").asInstanceOf[html.Div]
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
