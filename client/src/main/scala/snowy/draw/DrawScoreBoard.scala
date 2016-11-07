package snowy.draw

import snowy.GameClientProtocol.Scoreboard
import snowy.client.ClientDraw.{ctx, size}
import vector.Vec2d

class DrawScoreBoard(scale: Double, myName: String) {
  val scoresize   = Vec2d(210, 255)
  val scorescale  = Math.max(scoresize.x / size.x, scoresize.y / size.y) * 2.5
  val scorescaled = scoresize / scorescale
  val scorepos    = Vec2d(size.x * .99 - scorescaled.x, size.y * .01)
  ctx.fillStyle = "rgba(255, 255, 255, 0.5)"
  ctx.fillRect(scorepos.x, scorepos.y, scorescaled.x, scorescaled.y)

  ctx.fillStyle = "rgb(0, 0, 0)"
  val fromLeft  = 10 * scale
  val fromRight = 10 * scale
  val fromTop   = 60 * scale
  val inbetween = 20 * scale

  def draw(scoreboard: Scoreboard) {
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
