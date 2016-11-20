package snowy.draw

import snowy.client.ClientDraw.ctx
import snowy.draw.GameColors.lineColors
import snowy.playfield._
import vector.Vec2d

object DrawSled {
  def draw(name: String,
           pos: Vec2d,
           radius: Double,
           health: Double,
           cannonRotation: Double,
           rotation: Double,
           kind: SledKind,
           skiColor: String,
           color: String) {
    val x = pos.x
    val y = pos.y

    ctx.lineCap = "round"
    ctx.lineWidth = radius * 22 / 55
    ctx.strokeStyle = lineColors.toString
    drawSkis(x, y, radius, rotation)
    ctx.lineWidth = radius * 18 / 55
    ctx.strokeStyle = skiColor
    drawSkis(x, y, radius, rotation)

    //Global strokeStyle
    ctx.strokeStyle = lineColors.toString

    ctx.lineWidth = radius / 10
    ctx.fillStyle = "rgb(153, 153, 153)"
    kind match {
      case TankSled           => drawTankSled.drawTurret(x, y, radius, cannonRotation)
      case GunnerSled         => drawGunnerSled.drawTurret(x, y, radius, cannonRotation)
      case StationaryTestSled =>
      case _                  => drawDefaultSled.drawTurret(x, y, radius, cannonRotation)
    }

    ctx.fillStyle = color
    kind match {
      case SpeedySled => drawSpeedySled.drawBody(x, y, radius, cannonRotation)
      case SpikySled  => drawSpikySled.drawBody(x, y, radius, cannonRotation)
      case _          => drawDefaultSled.drawBody(x, y, radius, cannonRotation)
    }

    drawName(x, y, radius, name)
    if (health < 1) {
      drawHealth(x, y, radius, health)
    }
  }

  def drawSkis(x: Double, y: Double, radius: Double, rotation: Double): Unit = {
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
  }

  def drawName(x: Double, y: Double, radius: Double, name: String): Unit = {
    ctx.font = (radius * 6 / 11) + "px Arial"
    ctx.textAlign = "center"
    ctx.fillText(name, x, y - radius * 27 / 11)
  }

  def drawHealth(x: Double, y: Double, radius: Double, health: Double): Unit = {
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

  object drawDefaultSled {
    def drawBody(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {
      ctx.beginPath()
      ctx.arc(x, y, radius, 0, 2 * Math.PI)
      ctx.fill()
      ctx.stroke()
    }
    def drawTurret(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {
      ctx.beginPath()
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-radius * 3 / 11, 0, radius * 6 / 11, radius * 9 / 5)
      ctx.fill()
      ctx.stroke()
      ctx.setTransform(1, 0, 0, 1, 0, 0)
    }
  }

  object drawTankSled {
    def drawBody(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {}
    def drawTurret(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {
      ctx.beginPath()
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-radius * 7 / 11, 0, radius * 14 / 11, radius * 9 / 5)
      ctx.fill()
      ctx.stroke()
      ctx.setTransform(1, 0, 0, 1, 0, 0)
    }
  }

  object drawGunnerSled {
    def drawBody(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {}
    def drawTurret(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {
      ctx.beginPath()
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.rect(-radius * 3 / 11, 0, radius * 6 / 11, radius * 24 / 11)
      ctx.fill()
      ctx.stroke()
      ctx.setTransform(1, 0, 0, 1, 0, 0)
    }
  }

  object drawSpeedySled {
    def drawBody(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {
      ctx.beginPath()
      ctx.translate(x, y)
      ctx.rotate(cannonRotation)
      ctx.moveTo(-radius, -radius)
      ctx.lineTo(0, -radius * 0.6)
      ctx.lineTo(radius, -radius)
      ctx.lineTo(0, radius)
      ctx.closePath()
      ctx.fill()
      ctx.stroke()
      ctx.setTransform(1, 0, 0, 1, 0, 0)
    }
    def drawTurret(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {}
  }

  object drawSpikySled {
    def drawBody(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {
      ctx.beginPath()
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
      ctx.fill()
      ctx.stroke()
      ctx.setTransform(1, 0, 0, 1, 0, 0)
    }
    def drawTurret(x: Double, y: Double, radius: Double, cannonRotation: Double): Unit = {}
  }
}
