package snowy.draw

import org.scalajs.dom.{CanvasRenderingContext2D => Canvas}
import vector.Vec2d

class DrawSnowball(ctx: Canvas) {
  def apply(pos: Vec2d, size: Double): Unit = {
    ctx.strokeStyle = "rgb(100, 100, 100)"
    ctx.fillStyle = "rgb(208, 242, 237)"
    ctx.lineWidth = 2.5
    ctx.beginPath()
    ctx.arc(pos.x, pos.y, size, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()
  }
}