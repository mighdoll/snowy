package snowy.client

import org.scalajs.dom
import org.scalajs.dom._
import snowy.GameClientProtocol.Scoreboard
import snowy.GameConstants.PushEnergy.maxAmount
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

    val portal = new Portal(
      Rect(
        mySled.pos - Vec2d(750, 375),
        Vec2d(1500, 750)
      ),
      size,
      border
    )
    new DrawGrid(mySled.pos * portal.scale, portal.scale)

    snowballs.items.foreach { snowball =>
      val snowballPosition = portal.transformToScreen(snowball.pos)
      snowballPosition.foreach { pos =>
        DrawSnowball.draw(pos, snowball.radius * portal.scale)
      }
    }
    sleds.items.foreach { sled =>
      val sledPosition = portal.transformToScreen(sled.pos)
      sledPosition.foreach { pos =>
        DrawSled.draw(
          sled.userName,
          pos,
          sled.radius * portal.scale,
          sled.healthPercent,
          sled.turretRotation,
          sled.rotation,
          sled.kind,
          sled.skiColor.color.toString,
          bodyRed)
      }
    }

    ctx.fillStyle = "rgb(153, 153, 153)"
    ctx.translate((size / 2).x, (size / 2).y)
    ctx.rotate(-mySled.rotation)
    ctx.beginPath()
    ctx.moveTo(mySled.radius / 7, mySled.radius * 10 / 7)
    ctx.lineTo(-mySled.radius / 7, mySled.radius * 10 / 7)
    ctx.lineTo(0, mySled.radius * 12 / 7)
    ctx.closePath()
    ctx.fill()
    ctx.setTransform(1, 0, 0, 1, 0, 0)

    DrawSled.draw(
      mySled.userName,
      size / 2,
      mySled.radius * portal.scale,
      mySled.healthPercent,
      mySled.turretRotation,
      mySled.rotation,
      mySled.kind,
      mySled.skiColor.color.toString,
      bodyGreen)

    trees.items.foreach { tree =>
      val treePosition = portal.transformToScreen(tree.pos)
      treePosition.foreach { pos =>
        DrawTree.draw(pos, tree.size * 4 * portal.scale)
      }
    }

    new DrawMiniMap(border).draw(mySled, trees)

    val stats = Seq(
      s"Pushes: ${math.floor(mySled.pushEnergy * maxAmount)}",
      s"Health: ${math.floor(mySled.health * 100) / 10.0}",
      s"Pos: (${mySled._position.x.toInt}, ${mySled._position.y.toInt})"
    )
    new DrawStats(stats.length, portal.scale).draw(stats)

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
