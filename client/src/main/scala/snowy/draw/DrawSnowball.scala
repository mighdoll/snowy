package snowy.draw

import snowy.client.ClientDraw.ctx
import vector.Vec2d

class DrawSnowball(pos: Vec2d, size: Double) {
  ctx.strokeStyle = "rgb(100, 100, 100)"
  ctx.fillStyle = "rgb(208, 242, 237)"
  ctx.lineWidth = size / 10
  ctx.beginPath()
  ctx.arc(pos.x, pos.y, size, 0, 2 * Math.PI)
  ctx.fill()
  ctx.stroke()
}