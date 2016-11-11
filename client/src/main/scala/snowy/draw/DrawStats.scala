package snowy.draw

import snowy.client.ClientDraw.{ctx, size}
import vector.Vec2d

class DrawStats(amount: Int, scale: Double) {
  val padding  = 40 * scale
  val inbetween = 20 * scale

  val height = padding * 2 + inbetween * (amount - 1)
  val scorescaled = Vec2d(200 * scale, height)
  val scorepos    = Vec2d(size.x * .01, size.y * .99 - height)

  ctx.fillStyle = "rgba(255, 255, 255, 0.5)"
  ctx.fillRect(scorepos.x, scorepos.y, scorescaled.x, scorescaled.y)

  def draw(text: Seq[String]): Unit = {
    ctx.fillStyle = "rgb(0, 0, 0)"
    ctx.textAlign = "left"
    ctx.font = (15 * scale) + "px Arial"
    for ((line, index) <- text.zipWithIndex) {
      ctx.fillText(
        line,
        scorepos.x + padding,
        scorepos.y + padding + inbetween * index)
    }
  }
}
