package snowy.draw

import snowy.client.ClientDraw.ctx
import vector.Vec2d

class DrawSled(name: String, pos: Vec2d, health: Double, cannonRotation: Double, rotation: Double, color: String) {
  val x = pos.x
  val y = pos.y
  val turretSize = 35.0

  //Global strokeStyle
  ctx.strokeStyle = "rgb(100, 100, 100)"

  //Draw two skis
  ctx.lineCap = "round"
  ctx.lineWidth = turretSize * 9 / 55
  ctx.translate(x, y)
  ctx.rotate(-rotation)
  ctx.beginPath()
  ctx.moveTo(turretSize * 5 / 11, -turretSize * 25 / 22)
  ctx.lineTo(turretSize * 5 / 11, turretSize * 25 / 22)
  ctx.stroke()
  ctx.beginPath()
  ctx.moveTo(-turretSize * 5 / 11, -turretSize * 25 / 22)
  ctx.lineTo(-turretSize * 5 / 11, turretSize * 25 / 22)
  ctx.stroke()
  ctx.setTransform(1, 0, 0, 1, 0, 0)

  //Draw the barrel for snowballs
  ctx.translate(x + turretSize / 55, y)
  ctx.rotate(cannonRotation)
  ctx.lineWidth = 2.5
  ctx.fillStyle = "rgb(153, 153, 153)"
  ctx.beginPath()
  ctx.fillRect(-turretSize * 3 / 22, 0, turretSize * 3 / 11, turretSize * 9 / 10)
  ctx.strokeRect(-turretSize * 3 / 22, 0, turretSize * 3 / 11, turretSize * 9 / 10)
  ctx.fill()
  ctx.stroke()
  ctx.setTransform(1, 0, 0, 1, 0, 0)

  //Set the color for you vs other players
  ctx.fillStyle = color

  //Draw the main body
  ctx.beginPath()
  ctx.arc(x, y, turretSize / 2, 0, 2 * Math.PI)
  ctx.fill()
  ctx.stroke()

  //Draw the name
  ctx.font = (turretSize * 3 / 11) + "px Arial"
  ctx.beginPath()
  ctx.fillText(name, x - (ctx.measureText(name).width / 2), y - turretSize * 27 / 22)
  ctx.fill()

  if (health < 1) {
    ctx.lineWidth = turretSize * 3 / 25
    ctx.strokeStyle = "rgb(85, 85, 85)"
    ctx.beginPath()
    ctx.moveTo(x - turretSize * 2 / 5, y + turretSize * 5 / 5)
    ctx.lineTo(x + turretSize * 2 / 5, y + turretSize * 5 / 5)
    ctx.stroke()

    ctx.lineWidth = turretSize * 2 / 25
    ctx.strokeStyle = "rgb(134, 198, 128)"
    ctx.beginPath()
    ctx.moveTo(x - turretSize * 2 / 5, y + turretSize * 5 / 5)
    ctx.lineTo(x - turretSize * 2 / 5 + turretSize * 4 / 5 * health, y + turretSize * 5 / 5)
    ctx.stroke()
  }
}
