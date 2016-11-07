package snowy.client

import org.scalajs.dom
import org.scalajs.dom._
import snowy.GameClientProtocol.Scoreboard
import snowy.draw.GameColors.Sled._
import snowy.draw.GameColors.clearColor
import snowy.draw._
import snowy.playfield._
import vector.Vec2d

object ClientDraw {
  var size       = Vec2d(window.innerWidth, window.innerHeight)
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  val ctx =
    gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
  gameCanvas.width = size.x.toInt
  gameCanvas.height = size.y.toInt

  def drawState(snowballs: Store[Snowball],
                sleds: Store[Sled],
                mySled: Sled,
                trees: Store[Tree],
                border: Vec2d,
                scoreboard: Scoreboard): Unit = {
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
      new DrawSled(
        sled.userName,
        sled.pos,
        35 * portal.scale,
        sled.healthPercent,
        sled.turretRotation,
        sled.rotation,
        sled.kind,
        bodyRed)
    }
    new DrawSled(
      mySled.userName,
      size / 2,
      35 * portal.scale,
      mySled.healthPercent,
      mySled.turretRotation,
      mySled.rotation,
      mySled.kind,
      bodyGreen)

    portal.trees.foreach { tree =>
      new DrawTree(tree.pos, 100 * portal.scale)
    }

    new DrawMiniMap(border).draw(mySled, trees)

    new DrawScoreBoard(portal.scale, mySled.userName).draw(scoreboard)
  }

  def clearScreen(): Unit = {
    ctx.fillStyle = clearColor.toString
    ctx.fillRect(0, 0, size.x, size.y)
  }

  window.onresize = (_: UIEvent) => {
    size = Vec2d(window.innerWidth, window.innerHeight)
    gameCanvas.width = size.x.toInt
    gameCanvas.height = size.y.toInt

    clearScreen()
  }
}
