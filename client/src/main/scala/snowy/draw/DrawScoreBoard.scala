package snowy.draw

import snowy.GameClientProtocol.Scoreboard
import snowy.client.ClientDraw.{ctx, size}
import vector.Vec2d

class DrawScoreBoard(scale: Double, myName: String) {
  def draw(scoreboard: Scoreboard) {
    val scorescale  = Math.max(210 / size.x, 255 / size.y) * 2.5
    val scorescaled = Vec2d(210, 225) / scorescale
    val scorepos    = Vec2d(size.x * .99 - scorescaled.x, size.y * .01)
    ctx.fillStyle = "rgba(255, 255, 255, 0.5)"
    ctx.fillRect(scorepos.x, scorepos.y, scorescaled.x, scorescaled.y)

    ctx.fillStyle = "rgb(0, 0, 0)"
    val fromLeft  = 10 * scale
    val fromRight = 10 * scale
    val fromTop   = 60 * scale
    val inbetween = 20 * scale
    ctx.font = (25 * scale) + "px Arial"
    ctx.textAlign = "center"
    ctx
      .fillText("Scoreboard", scorepos.x + scorescaled.x / 2, scorepos.y + inbetween * 2)
    ctx.font = (15 * scale) + "px Arial"
    for ((score, index) <- scoreboard.scores.sortWith(_.score > _.score).zipWithIndex) {
      ctx.textAlign = "left"
      ctx.fillText(
        index + 1 + ".",
        scorepos.x + fromLeft,
        scorepos.y + fromTop + inbetween * index)
      ctx.fillText(
        score.userName,
        scorepos.x + fromLeft * 4,
        scorepos.y + fromTop + inbetween * index)
      ctx.textAlign = "right"
      ctx.fillText(
        score.score.toInt.toString,
        scorepos.x + scorescaled.x - fromRight,
        scorepos.y + fromTop + inbetween * index)
    }
    ctx.textAlign = "left"
    ctx.fillText(myName, scorepos.x + fromLeft, scorepos.y + scorescaled.y - inbetween)
    ctx.textAlign = "right"
    ctx.fillText(
      scoreboard.myScore.toInt.toString,
      scorepos.x + scorescaled.x - fromRight,
      scorepos.y + scorescaled.y - inbetween)
  }
}
