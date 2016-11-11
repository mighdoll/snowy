package snowy.draw

import snowy.client.ClientDraw.ctx
import snowy.draw.GameColors.Tree._
import vector.Vec2d

object DrawTree {
  def draw(pos: Vec2d, size: Double) {
    ctx.fillStyle = leaves
    drawLeaves(pos.x, pos.y, size)
    ctx.fillStyle = trunk
    drawBark(pos.x, pos.y, size)
  }

  def drawLeaves(x: Double, y: Double, size: Double) {
    ctx.beginPath()
    ctx.moveTo(x, y - size * 2)
    ctx.lineTo(x - size * 1 / 2, y - size * 1 / 2)
    ctx.lineTo(x + size * 1 / 2, y - size * 1 / 2)
    ctx.closePath()
    ctx.fill()
  }

  def drawBark(x: Double, y: Double, size: Double) {
    ctx.fillRect(x - size * 1 / 16, y - size * 3 / 4, size * 1 / 8, size * 3 / 4)
    ctx.translate(x, y - size * 29 / 50)
    ctx.rotate(Math.PI * 13 / 18)
    ctx.fillRect(0, 0, size * 1 / 20, size * 43 / 200)
    ctx.rotate(Math.PI * 5 / 9)
    ctx.fillRect(0, size * 7 / 200, size * 1 / 20, size * 1 / 4)
    ctx.fillRect(size * -1 / 10, size * -13 / 200, size * 1 / 20, size * 1 / 4)
    ctx.setTransform(1, 0, 0, 1, 0, 0)
  }
}
