package snowy.draw

import snowy.client.ClientDraw.ctx
import snowy.draw.GameColors._
import vector.Vec2d

object DrawSnowball {
  def draw(pos: Vec2d, size: Double): Unit = {
    ctx.strokeStyle = lineColors.toString
    ctx.fillStyle = snowball
    ctx.lineWidth = size / 10
    ctx.beginPath()
    ctx.arc(pos.x, pos.y, size, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()
  }
}
