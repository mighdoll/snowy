package snowy.client

import org.scalajs.dom
import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.playfield._
import vector.Vec2d
import snowy.draw._
import snowy.draw.GameColors.Sled._
import snowy.draw.GameColors.clearColor

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

  def drawState(snowballs: Store[Snowball], sleds: Store[Sled], mySled: Sled, trees: Trees, border: Playfield): Unit = {
    clearScreen()
    val center = new Center(mySled.pos, border)

    new DrawBorder(screenPosition(Vec2d(0, 0), mySled.pos), screenPosition(Vec2d(border.width, border.height), mySled.pos), mySled.pos)

    //Draw all snowballs
    snowballs.items.foreach { snowball =>
      new DrawSnowball(center(snowball.pos), snowball.size / 2)
    }

    //Draw all sleds
    sleds.items.foreach { sled =>
      new DrawSled(sled.userName, center(sled.pos), 35, sled.health, sled.turretRotation, sled.rotation, bodyRed)
    }
    new DrawSled(mySled.userName, Vec2d(size.width / 2, size.height / 2), 35, mySled.health, mySled.turretRotation, mySled.rotation, bodyGreen)

    trees.trees.foreach { tree =>
      new DrawTree(center(tree.pos))
    }
  }

  class Center(sled: Vec2d, border: Playfield) {
    def apply(pos: Vec2d): Vec2d  = {
      fancyBorderWrap(screenPosition(pos, sled), border)
    }
  }

  /** @param pos position of an object in game coordinates
    * @return screen position of given object
    * taking into account sled being centered on the screen
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
