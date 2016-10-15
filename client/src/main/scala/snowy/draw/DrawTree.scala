package snowy.draw

import snowy.client.ClientDraw.ctx
import vector.Vec2d

class DrawTree(pos: Vec2d) {
  val x = pos.x
  val y = pos.y
  val branchSize = 100

  ctx.fillStyle = "rgb(94, 153, 105)"
  ctx.beginPath()
  ctx.moveTo(x, y - branchSize * 2)
  ctx.lineTo(x - branchSize * 1 / 2, y - branchSize * 1 / 2)
  ctx.lineTo(x + branchSize * 1 / 2, y - branchSize * 1 / 2)
  ctx.closePath()
  ctx.fill()

  ctx.fillStyle = "rgb(56, 85, 58)"
  ctx.beginPath()
  ctx.fillRect(x - branchSize * 1 / 16, y - branchSize * 3 / 4, branchSize * 1 / 8, branchSize * 3 / 4)
  ctx.fill()

  ctx.translate(x, y - branchSize * 29 / 50)
  ctx.rotate(Math.PI * 13 / 18)

  ctx.beginPath()
  ctx.fillRect(0, 0, branchSize * 1 / 20, branchSize * 43 / 200)
  ctx.fill()

  ctx.rotate(Math.PI * 5 / 9)
  ctx.beginPath()
  ctx.fillRect(0, branchSize * 7 / 200, branchSize * 1 / 20, branchSize * 1 / 4)
  ctx.fillRect(branchSize * -1 / 10, branchSize * -13 / 200, branchSize * 1 / 20, branchSize * 1 / 4)
  ctx.fill()

  ctx.setTransform(1, 0, 0, 1, 0, 0)
}
