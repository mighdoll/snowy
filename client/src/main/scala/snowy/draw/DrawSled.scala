package snowy.draw

import snowy.client.ClientDraw.ctx
import snowy.draw.GameColors.lineColors
import snowy.playfield._
import vector.Vec2d

class DrawSled(name: String,
               pos: Vec2d,
               size: Double,
               health: Double,
               cannonRotation: Double,
               rotation: Double,
               kind: SledKind,
               color: String) {
  val x = pos.x
  val y = pos.y

  //Global strokeStyle
  ctx.strokeStyle = lineColors.toString

  //Draw two skis
  ctx.lineCap = "round"
  ctx.lineWidth = size * 9 / 55
  ctx.translate(x, y)
  ctx.rotate(-rotation)
  ctx.beginPath()
  ctx.moveTo(size * 5 / 11, -size * 25 / 22)
  ctx.lineTo(size * 5 / 11, size * 25 / 22)
  ctx.stroke()
  ctx.beginPath()
  ctx.moveTo(-size * 5 / 11, -size * 25 / 22)
  ctx.lineTo(-size * 5 / 11, size * 25 / 22)
  ctx.stroke()
  ctx.setTransform(1, 0, 0, 1, 0, 0)

  //Draw the barrel for snowballs
  ctx.lineWidth = size / 20
  ctx.fillStyle = "rgb(153, 153, 153)"
  ctx.beginPath()
  kind match {
    case TankSled =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-size * 7 / 22, 0, size * 7 / 11, size * 9 / 10)
    case GunnerSled =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-size * 3 / 22, 0, size * 3 / 11, size * 12 / 11)
    case StationaryTestSled =>
    case _ =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-size * 3 / 22, 0, size * 3 / 11, size * 9 / 10)
  }
  ctx.fill()
  ctx.stroke()
  ctx.setTransform(1, 0, 0, 1, 0, 0)

  //Set the color for you vs other players
  ctx.fillStyle = color

  //Draw the main body
  ctx.beginPath()
  kind match {
    case SpeedySled =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.moveTo(-size / 2, -size / 2)
      ctx.lineTo(0, -size * 0.3)
      ctx.lineTo(size / 2, -size / 2)
      ctx.lineTo(0, size / 2)
      ctx.closePath()
    case SpikySled =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.moveTo(size * 3 / 8, 0)
      ctx.lineTo(size / 2, 0)
      ctx.lineTo(size * 10.6 / 40, size * 10.6 / 40)
      ctx.lineTo(size * 28.3 / 80, size * 28.3 / 80)
      ctx.lineTo(0, size * 3 / 8)
      ctx.lineTo(0, size / 2)
      ctx.lineTo(-size * 10.6 / 40, size * 10.6 / 40)
      ctx.lineTo(-size * 28.3 / 80, size * 28.3 / 80)
      ctx.lineTo(-size * 3 / 8, 0)
      ctx.lineTo(-size / 2, 0)
      ctx.lineTo(-size * 10.6 / 40, -size * 10.6 / 40)
      ctx.lineTo(-size * 28.3 / 80, -size * 28.3 / 80)
      ctx.lineTo(0, -size * 3 / 8)
      ctx.lineTo(0, -size / 2)
      ctx.lineTo(size * 10.6 / 40, -size * 10.6 / 40)
      ctx.lineTo(size * 28.3 / 80, -size * 28.3 / 80)
      ctx.lineTo(size * 3 / 8, 0)
      ctx.closePath()
    case _ => ctx.arc(x, y, size / 2, 0, 2 * Math.PI)
  }
  ctx.fill()
  ctx.stroke()
  ctx.setTransform(1, 0, 0, 1, 0, 0)

  //Draw the name
  ctx.font = (size * 3 / 11) + "px Arial"
  ctx.textAlign = "center"
  ctx.fillText(name, x, y - size * 27 / 22)

  if (health < 1) {
    ctx.lineWidth = size * 3 / 25
    ctx.strokeStyle = "rgb(85, 85, 85)"
    ctx.beginPath()
    ctx.moveTo(x - size * 2 / 5, y + size * 5 / 5)
    ctx.lineTo(x + size * 2 / 5, y + size * 5 / 5)
    ctx.stroke()

    ctx.lineWidth = size * 2 / 25
    ctx.strokeStyle = "rgb(134, 198, 128)"
    ctx.beginPath()
    ctx.moveTo(x - size * 2 / 5, y + size * 5 / 5)
    ctx.lineTo(x - size * 2 / 5 + size * 4 / 5 * health, y + size * 5 / 5)
    ctx.stroke()
  }
}
