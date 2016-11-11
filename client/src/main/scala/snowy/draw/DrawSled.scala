package snowy.draw

import snowy.client.ClientDraw.ctx
import snowy.draw.GameColors.lineColors
import snowy.playfield._
import vector.Vec2d

class DrawSled(name: String,
               pos: Vec2d,
               radius: Double,
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
  ctx.lineWidth = radius * 18 / 55
  ctx.translate(x, y)
  ctx.rotate(-rotation)
  ctx.beginPath()
  ctx.moveTo(radius * 10 / 11, -radius * 25 / 11)
  ctx.lineTo(radius * 10 / 11, radius * 25 / 11)
  ctx.stroke()
  ctx.beginPath()
  ctx.moveTo(-radius * 10 / 11, -radius * 25 / 11)
  ctx.lineTo(-radius * 10 / 11, radius * 25 / 11)
  ctx.stroke()
  ctx.setTransform(1, 0, 0, 1, 0, 0)

  //Draw the barrel for snowballs
  ctx.lineWidth = radius / 10
  ctx.fillStyle = "rgb(153, 153, 153)"
  ctx.beginPath()
  kind match {
    case TankSled =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-radius * 7 / 11, 0, radius * 14 / 11, radius * 9 / 5)
    case GunnerSled =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-radius * 3 / 11, 0, radius * 6 / 11, radius * 24 / 11)
    case StationaryTestSled =>
    case _ =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-radius * 3 / 11, 0, radius * 6 / 11, radius * 9 / 5)
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
      ctx.moveTo(-radius, -radius)
      ctx.lineTo(0, -radius * 0.6)
      ctx.lineTo(radius, -radius)
      ctx.lineTo(0, radius)
      ctx.closePath()
    case SpikySled =>
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.moveTo(radius * 3 / 4, 0)
      ctx.lineTo(radius, 0)
      ctx.lineTo(radius * 21.2 / 40, radius * 21.2 / 40)
      ctx.lineTo(radius * 28.3 / 40, radius * 28.3 / 40)
      ctx.lineTo(0, radius * 3 / 4)
      ctx.lineTo(0, radius)
      ctx.lineTo(-radius * 21.2 / 40, radius * 21.2 / 40)
      ctx.lineTo(-radius * 28.3 / 40, radius * 28.3 / 40)
      ctx.lineTo(-radius * 3 / 4, 0)
      ctx.lineTo(-radius, 0)
      ctx.lineTo(-radius * 21.2 / 40, -radius * 21.2 / 40)
      ctx.lineTo(-radius * 28.3 / 40, -radius * 28.3 / 40)
      ctx.lineTo(0, -radius * 3 / 4)
      ctx.lineTo(0, -radius)
      ctx.lineTo(radius * 21.2 / 40, -radius * 21.2 / 40)
      ctx.lineTo(radius * 28.3 / 40, -radius * 28.3 / 40)
      ctx.lineTo(radius * 3 / 4, 0)
      ctx.closePath()
    case _ => ctx.arc(x, y, radius, 0, 2 * Math.PI)
  }
  ctx.fill()
  ctx.stroke()
  ctx.setTransform(1, 0, 0, 1, 0, 0)

  //Draw the name
  ctx.font = (radius * 6 / 11) + "px Arial"
  ctx.textAlign = "center"
  ctx.fillText(name, x, y - radius * 27 / 11)

  if (health < 1) {
    ctx.lineWidth = radius * 6 / 25
    ctx.strokeStyle = "rgb(85, 85, 85)"
    ctx.beginPath()
    ctx.moveTo(x - radius * 4 / 5, y + radius * 2)
    ctx.lineTo(x + radius * 4 / 5, y + radius * 2)
    ctx.stroke()

    ctx.lineWidth = radius * 4 / 25
    ctx.strokeStyle = "rgb(134, 198, 128)"
    ctx.beginPath()
    ctx.moveTo(x - radius * 4 / 5, y + radius * 2)
    ctx.lineTo(x - radius * 4 / 5 + radius * 8 / 5 * health, y + radius * 2)
    ctx.stroke()
  }
}
