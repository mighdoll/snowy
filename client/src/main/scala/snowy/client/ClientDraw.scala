package snowy.client

import org.scalajs.dom
import org.scalajs.dom._
import snowy.draw.GameColors.Sled._
import snowy.draw.GameColors.clearColor
import snowy.draw._
import snowy.playfield._
import vector.Vec2d

object ClientDraw {
  var size = Vec2d(window.innerWidth, window.innerHeight)
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  gameCanvas.width = size.x.toInt
  gameCanvas.height = size.y.toInt
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  def drawState(snowballs: Store[Snowball], sleds: Store[Sled], mySled: Sled, trees: Store[Tree], border: Vec2d): Unit = {
    clearScreen()

    var portal = new Portal(
      Rect(
        mySled.pos - Vec2d(1000, 500),
        Vec2d(2000, 1000)
      )
    )(sleds.items, snowballs.items, trees.items)

    portal = portal.convertToScreen(size, border)
    new DrawGrid(mySled.pos * portal.scale, portal.scale)


    portal.snowballs.foreach { snowball =>
      new DrawSnowball(snowball.pos, snowball.size / 2 * portal.scale)
    }
    portal.sleds.foreach { sled =>
      new DrawSled(sled.userName, sled.pos, 35 * portal.scale, sled.health, sled.turretRotation, sled.rotation, bodyRed)
    }
    new DrawSled(mySled.userName, size / 2, 35 * portal.scale, mySled.health, mySled.turretRotation, mySled.rotation, bodyGreen)

    portal.trees.foreach { tree =>
      new DrawTree(tree.pos, 100 * portal.scale)
    }

    var minimap = new Portal(
      Rect(
        Vec2d(0, 0),
        border
      )
    )(Set(mySled), Set(), trees.items)
    val scale = Math.max(border.x / size.x, border.y / size.y) * 2
    minimap = minimap.convertToScreen(border / scale, border)
    val scaled = border / scale
    val offset = size * .95 - scaled
    ctx.beginPath()
    ctx.fillStyle = "rgba(255, 255, 255, 0.5)"
    ctx.fillRect(offset.x, offset.y, scaled.x, scaled.y)
    ctx.fill()
    minimap.trees.foreach { tree =>
      val newPos = tree.pos + offset
      new DrawTree(newPos, 50 * minimap.scale)
    }
    minimap.sleds.foreach { sled =>
      val newPos = sled.pos + offset
      new DrawSled(sled.userName, newPos, 70 * minimap.scale, sled.health, sled.turretRotation, sled.rotation, bodyRed)
    }
  }

  def clearScreen(): Unit = {
    ctx.fillStyle = clearColor
    ctx.fillRect(0, 0, size.x, size.y)
    ctx.fill()
  }

  case class Size(width: Int, height: Int)

  window.onresize = (_: UIEvent) => {
    size = Vec2d(window.innerWidth, window.innerHeight)
    gameCanvas.width = size.x.toInt
    gameCanvas.height = size.y.toInt

    clearScreen()
  }
}
