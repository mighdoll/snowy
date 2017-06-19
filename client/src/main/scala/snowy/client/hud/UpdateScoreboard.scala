package snowy.client.hud

import org.scalajs.dom._
import snowy.GameClientProtocol.Scoreboard
import snowy.connection.GameState
import snowy.playfield.Sled

class UpdateScoreboard(gameState: GameState) {
  private val scoreTable =
    document.getElementById("scores").asInstanceOf[html.Div]
  val myScoreElement = document.getElementById("my-score").asInstanceOf[html.Div]
  val myScoreName    = myScoreElement.children(0)
  val mySledScore    = myScoreElement.children(1)

  for (_ <- 1 to 10) {
    val scoreElement = document.createElement("div").asInstanceOf[html.Div]
    val scoreName    = document.createElement("div").asInstanceOf[html.Div]
    val scoreValue   = document.createElement("div").asInstanceOf[html.Div]

    scoreElement.classList.add("score-element")
    scoreName.classList.add("score-name")
    scoreValue.classList.add("score-value")

    scoreElement.appendChild(scoreName)
    scoreElement.appendChild(scoreValue)
    scoreTable.appendChild(scoreElement)
  }

  def updateScoreboard(scoreboard: Scoreboard): Unit = {
    scoreboard.scores.sortWith(_.score > _.score).zipWithIndex.foreach {
      case (score, index) =>
        val scoreElement = scoreTable.children(index)
        val scoreName    = scoreElement.children(0)
        val scoreValue   = scoreElement.children(1)

        scoreName.innerHTML = score.userName
        scoreValue.innerHTML = math.floor(score.score).toString
    }

    myScoreName.innerHTML = gameState.serverMySled.getOrElse(Sled.dummy).userName
    mySledScore.innerHTML = math.floor(scoreboard.myScore).toString
  }
}
