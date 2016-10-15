package snowy.draw

import snowy.client.ClientDraw._
import vector.Vec2d


class DrawBorder(top: Vec2d, bottom: Vec2d, sled: Vec2d) {
  ctx.strokeStyle = "rgb(100, 100, 100)"
  ctx.beginPath()
  ctx.moveTo(top.x, top.y)
  ctx.lineTo(top.x, bottom.y)
  ctx.lineTo(bottom.x, bottom.y)
  ctx.lineTo(bottom.x, top.y)
  ctx.closePath()
  ctx.stroke()

  val lineGap = 10
  ctx.lineWidth = .1
  for (i <- 0 to size.width / lineGap) {
    ctx.beginPath()
    ctx.moveTo(i * lineGap - sled.x % lineGap, 0)
    ctx.lineTo(i * lineGap - sled.x % lineGap, size.height)
    ctx.stroke()
  }
  for (i <- 0 to size.height / lineGap) {
    ctx.beginPath()
    ctx.moveTo(0, i * lineGap - sled.y % lineGap)
    ctx.lineTo(size.width, i * lineGap - sled.y % lineGap)
    ctx.stroke()
  }
}
