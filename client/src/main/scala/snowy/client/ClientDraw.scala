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

    portal = portal.
      translateToPortal().
      portalToScreen(size.width, size.height)

    new DrawGrid(screenPosition(Vec2d(0, 0), mySled.pos * portal.scale), screenPosition(Vec2d(border.width, border.height), mySled.pos * portal.scale), mySled.pos * portal.scale, portal.scale)

    portal.snowballs.foreach { snowball =>
      new DrawSnowball(snowball.pos, portal.scale * snowball.size / 2)
    }
    portal.sleds.foreach { sled =>
      new DrawSled(sled.userName, sled.pos, portal.scale * 35, sled.health, sled.turretRotation, sled.rotation, bodyRed)
    }
    new DrawSled(mySled.userName, Vec2d(size.width / 2, size.height / 2), 35 * portal.scale, mySled.health, mySled.turretRotation, mySled.rotation, bodyGreen)

    portal.trees.foreach { tree =>
      new DrawTree(tree.pos, portal.scale * 100)
    }
  }

  /** @param pos position of an object in game coordinates
    * @return screen position of given object
    *         taking into account sled being centered on the screen
    */
  def screenPosition(pos: Vec2d, me: Vec2d): Vec2d = {
    Vec2d(pos.x - me.x + size.width / 2, pos.y - me.y + size.height / 2)
  }

  /** @return object wrapped over border */
  def fancyBorderWrap(pos: Vec2d, playField: Playfield): Vec2d = {
    var x = pos.x
    var y = pos.y

    if (x > size.width + 200) {
      x = x - playField.width
    } else if (x < -200) {
      x = x + playField.width
    }
    if (y > size.height + 200) {
      y = y - playField.height
    } else if (y < -200) {
      y = y + playField.height
    }

    Vec2d(x, y)
  }

  window.onresize = (_: UIEvent) => {
    size = Size(window.innerWidth, window.innerHeight)
    gameCanvas.width = size.width
    gameCanvas.height = size.height

    clearScreen()
  }
}
