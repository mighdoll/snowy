package snowy.client

import org.scalajs.dom
import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.draw.GameColors.Sled._
import snowy.draw.GameColors.clearColor
import snowy.draw._
import snowy.playfield._
import vector.Vec2d

object ClientDraw {

  case class Size(width: Int, height: Int)

  var size = Size(window.innerWidth, window.innerHeight)
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
  gameCanvas.width = size.width
  gameCanvas.height = size.height

  def clearScreen(): Unit = {
    ctx.fillStyle = clearColor
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()
  }

  def drawState(snowballs: Store[Snowball], sleds: Store[Sled], mySled: Sled, trees: Store[Tree], border: Playfield): Unit = {
    clearScreen()

    var portal = new Portal(
      Rect(
        mySled.pos - Vec2d(1000, 500),
        Vec2d(2000, 1000)
      )
    )(sleds.items, snowballs.items, trees.items)

    portal = portal.convertToScreen(Vec2d(size.width, size.height), Vec2d(border.width, border.height))
    new DrawGrid(mySled.pos * portal.scale, portal.scale)

    portal.snowballs.foreach { snowball =>
      new DrawSnowball(snowball.pos, snowball.size / 2 * portal.scale)
    }
    portal.sleds.foreach { sled =>
      new DrawSled(sled.userName, sled.pos, 35 * portal.scale, sled.health, sled.turretRotation, sled.rotation, bodyRed)
    }
    new DrawSled(mySled.userName, Vec2d(size.width / 2, size.height / 2), 35 * portal.scale, mySled.health, mySled.turretRotation, mySled.rotation, bodyGreen)

    portal.trees.foreach { tree =>
      new DrawTree(tree.pos, 100 * portal.scale)
    }

    var minimap = new Portal(
      Rect(
        Vec2d(0, 0),
        Vec2d(border.width, border.height)
      )
    )(Set(mySled), Set(), trees.items)
    val scale = Math.max(border.width.toDouble / size.width, border.height.toDouble / size.height) * 2
    minimap = minimap.convertToScreen(Vec2d(border.width / scale, border.height / scale), Vec2d(border.width, border.height))
    minimap.trees.foreach { tree =>
      val newPos = tree.pos + Vec2d(size.width, size.height) * .95 - Vec2d(border.width / scale, border.height / scale)
      new DrawTree(newPos, 50 * minimap.scale)
    }
    minimap.sleds.foreach { sled =>
      val newPos = sled.pos + Vec2d(size.width, size.height) * .95 - Vec2d(border.width / scale, border.height / scale)
      new DrawSled(sled.userName, newPos, 70 * minimap.scale, sled.health, sled.turretRotation, sled.rotation, bodyRed)
    }
  }

  window.onresize = (_: UIEvent) => {
    size = Size(window.innerWidth, window.innerHeight)
    gameCanvas.width = size.width
    gameCanvas.height = size.height

    clearScreen()
  }
}
