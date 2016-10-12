import snowy.GameClientProtocol._
import draw._
import org.scalajs.dom
import org.scalajs.dom._
import vector.Vec2d

object ClientDraw {
  var size = Size(window.innerWidth, window.innerHeight)
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
  gameCanvas.width = size.width
  gameCanvas.height = size.height

  val drawTree = new DrawTree(ctx)
  val drawSled = new DrawSled(ctx)
  val drawSnowball = new DrawSnowball(ctx)
  val drawBorder = new DrawBorder(ctx, size)

  def clearScreen(): Unit = {
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()
  }

  class snowFlake(index: Double) {
    val flakeSize = Math.random() * 5 + 5
    var x = index * 10
    var y = -Math.random() * size.height

    def move(): Unit = {
      y += 1
      y = y % size.height
    }

    def draw(): Unit = {
      ctx.fillStyle = "#b3e5fc"
      ctx.beginPath()
      ctx.arc(x, y, flakeSize, 0, 2 * Math.PI)
      ctx.fill()
    }
  }

  def drawState(state: State, trees: Trees, border: Playfield): Unit = {
    clearScreen()
    val center = new Center(state.mySled.pos, border)

    drawBorder(screenPosition(Vec2d(0, 0), state.mySled.pos), screenPosition(Vec2d(border.width, border.height), state.mySled.pos), state.mySled.pos)

    //Draw all snowballs
    state.snowballs.foreach { snowball =>
      drawSnowball(center(snowball.pos), snowball.size / 2)
    }
    //Draw all sleds
    state.sleds.foreach { sled =>
      drawSled(sled.userName, center(sled.pos), sled.health, sled.turretRotation, sled.rotation, "rgb(241, 78, 84)")
    }
    drawSled(state.mySled.userName, Vec2d(size.width / 2, size.height / 2), state.mySled.health, state.mySled.turretRotation, state.mySled.rotation, "rgb(120, 201, 44)")

    trees.trees.foreach { tree =>
      drawTree(center(tree.pos))
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
