import GameClientProtocol._
import org.scalajs.dom
import org.scalajs.dom._

object ClientDraw {

  case class Size(width: Int, height: Int)

  var size = Size(window.innerWidth, window.innerHeight)
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
  gameCanvas.width = size.width
  gameCanvas.height = size.height

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

    drawBorder(centerObject(Vec2d(0, 0), state.mySled.pos), centerObject(Vec2d(border.width, border.height), state.mySled.pos), state.mySled.pos)

    //Draw all snowballs
    state.snowballs.foreach { snowball =>
      drawSnowball(screenPosition(centerObject(snowball.pos, state.mySled.pos), border), snowball.size / 2)
    }
    //Draw all sleds
    state.sleds.foreach { sled =>
      drawSled(sled.userName, screenPosition(centerObject(sled.pos, state.mySled.pos), border), sled.health, sled.turretRotation, sled.rotation, "rgb(241, 78, 84)")
    }
    drawSled(state.mySled.userName, screenPosition(Vec2d(size.width / 2, size.height / 2), border), state.mySled.health, state.mySled.turretRotation, state.mySled.rotation, "rgb(120, 201, 44)")

    trees.trees.foreach { tree =>
      drawTree(screenPosition(centerObject(tree.pos, state.mySled.pos), border))
    }
  }

  //Draw a sled at an x and y
  def drawSled(name: String, pos: Vec2d, health: Double, cannonRotation: Double, rotation: Double, color: String): Unit = {
    val x = pos.x
    val y = pos.y
    val turretSize = 35.0

    //Global strokeStyle
    ctx.strokeStyle = "rgb(100, 100, 100)"

    //Draw two skis
    ctx.lineCap = "round"
    ctx.lineWidth = turretSize * 9 / 55
    ctx.translate(x, y)
    ctx.rotate(-rotation)
    ctx.beginPath()
    ctx.moveTo(turretSize * 5 / 11, -turretSize * 25 / 22)
    ctx.lineTo(turretSize * 5 / 11, turretSize * 25 / 22)
    ctx.stroke()
    ctx.beginPath()
    ctx.moveTo(-turretSize * 5 / 11, -turretSize * 25 / 22)
    ctx.lineTo(-turretSize * 5 / 11, turretSize * 25 / 22)
    ctx.stroke()
    ctx.setTransform(1, 0, 0, 1, 0, 0)

    //Draw the barrel for snowballs
    ctx.translate(x + turretSize / 55, y)
    ctx.rotate(cannonRotation)
    ctx.lineWidth = 2.5
    ctx.fillStyle = "rgb(153, 153, 153)"
    ctx.beginPath()
    ctx.fillRect(-turretSize * 3 / 22, 0, turretSize * 3 / 11, turretSize * 9 / 10)
    ctx.strokeRect(-turretSize * 3 / 22, 0, turretSize * 3 / 11, turretSize * 9 / 10)
    ctx.fill()
    ctx.stroke()
    ctx.setTransform(1, 0, 0, 1, 0, 0)

    //Set the color for you vs other players
    ctx.fillStyle = color

    //Draw the main body
    ctx.beginPath()
    ctx.arc(x, y, turretSize / 2, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()

    //Draw the name
    ctx.font = (turretSize * 3 / 11) + "px Arial"
    ctx.beginPath()
    ctx.fillText(name, x - (ctx.measureText(name).width / 2), y - turretSize * 27 / 22)
    ctx.fill()

    if (health < 1) {
      ctx.lineWidth = turretSize * 3 / 25
      ctx.strokeStyle = "rgb(85, 85, 85)"
      ctx.beginPath()
      ctx.moveTo(x - turretSize * 2 / 5, y + turretSize * 5 / 5)
      ctx.lineTo(x + turretSize * 2 / 5, y + turretSize * 5 / 5)
      ctx.stroke()

      ctx.lineWidth = turretSize * 2 / 25
      ctx.strokeStyle = "rgb(134, 198, 128)"
      ctx.beginPath()
      ctx.moveTo(x - turretSize * 2 / 5, y + turretSize * 5 / 5)
      ctx.lineTo(x - turretSize * 2 / 5 + turretSize * 4 / 5 * health, y + turretSize * 5 / 5)
      ctx.stroke()
    }
  }

  //Draw a tree on the canvas
  def drawTree(pos: Vec2d): Unit = {
    val x = pos.x
    val y = pos.y
    val branchSize = 100

    ctx.fillStyle = "rgb(94, 153, 105)"
    ctx.beginPath()
    ctx.moveTo(x, y - branchSize * 2)
    ctx.lineTo(x - branchSize * 1 / 2, y - branchSize * 1 / 2)
    ctx.lineTo(x + branchSize * 1 / 2, y - branchSize * 1 / 2)
    ctx.closePath()
    ctx.fill()

    ctx.fillStyle = "rgb(56, 85, 58)"
    ctx.beginPath()
    ctx.fillRect(x - branchSize * 1 / 16, y - branchSize * 3 / 4, branchSize * 1 / 8, branchSize * 3 / 4)
    ctx.fill()

    ctx.translate(x, y - branchSize * 29 / 50)
    ctx.rotate(Math.PI * 13 / 18)

    ctx.beginPath()
    ctx.fillRect(0, 0, branchSize * 1 / 20, branchSize * 43 / 200)
    ctx.fill()

    ctx.rotate(Math.PI * 5 / 9)
    ctx.beginPath()
    ctx.fillRect(0, branchSize * 7 / 200, branchSize * 1 / 20, branchSize * 1 / 4)
    ctx.fillRect(branchSize * -1 / 10, branchSize * -13 / 200, branchSize * 1 / 20, branchSize * 1 / 4)
    ctx.fill()

    ctx.setTransform(1, 0, 0, 1, 0, 0)
  }

  def drawSnowball(pos: Vec2d, size: Double): Unit = {
    ctx.strokeStyle = "rgb(100, 100, 100)"
    ctx.fillStyle = "rgb(208, 242, 237)"
    ctx.lineWidth = 2.5
    ctx.beginPath()
    ctx.arc(pos.x, pos.y, size, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()
  }

  def drawBorder(top: Vec2d, bottom: Vec2d, sled: Vec2d): Unit = {
    ctx.strokeStyle = "rgb(100, 100, 100)"
    ctx.beginPath()
    ctx.moveTo(top.x, top.y)
    ctx.lineTo(top.x, bottom.y)
    ctx.lineTo(bottom.x, bottom.y)
    ctx.lineTo(bottom.x, top.y)
    ctx.closePath()
    ctx.stroke()

    val lineGap = 10
    ctx.lineWidth = .1
    for (i <- 0 to size.width / lineGap) {
      ctx.beginPath()
      ctx.moveTo(i * lineGap - sled.x % lineGap, 0)
      ctx.lineTo(i * lineGap - sled.x % lineGap, size.height)
      ctx.stroke()
    }
    for (i <- 0 to size.height / lineGap) {
      ctx.beginPath()
      ctx.moveTo(0, i * lineGap - sled.y % lineGap)
      ctx.lineTo(size.width, i * lineGap - sled.y % lineGap)
      ctx.stroke()
    }
  }

  def centerObject(pos: Vec2d, me: Vec2d): Vec2d = {
    Vec2d(pos.x - me.x + size.width / 2, pos.y - me.y + size.height / 2)
  }

  def screenPosition(pos: Vec2d, playField: GameClientProtocol.Playfield): Vec2d = {
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
