import ClientMain.Size
import GameClientProtocol._
import org.scalajs.dom
import org.scalajs.dom._

object ClientDraw {
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

  def drawState(state: State, trees: Trees, border: PlayField): Unit = {
    //Draw all snowballs
    state.snowballs.foreach { snowball =>
      drawSnowball(screenPosition(centerObject(snowball.position, state.mySled.position), border), 10.0)
    }
    //Draw all sleds
    drawSled("me", screenPosition(Position(size.width / 2, size.height * 2 / 3), border), state.mySled.turretRotation, state.mySled.rotation)
    state.sleds.foreach { sled =>
      drawSled(sled.userName, screenPosition(centerObject(sled.position, state.mySled.position), border), sled.turretRotation, sled.rotation)
    }

    trees.trees.foreach { tree =>
      drawTree(screenPosition(centerObject(tree.position, state.mySled.position), border))
    }

    drawBorder(centerObject(GameClientProtocol.Position(0, 0), state.mySled.position), centerObject(GameClientProtocol.Position(border.width, border.height), state.mySled.position))
  }

  //Draw a sled at an x and y
  def drawSled(name: String, pos: GameClientProtocol.Position, cannonRotation: Double, rotation: Double): Unit = {
    val x = pos.x
    val y = pos.y
    val turretSize = 50.0

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
    ctx.lineWidth = 2.0
    ctx.fillStyle = "rgb(153, 153, 153)"
    ctx.beginPath()
    ctx.fillRect(-turretSize * 3 / 22, 0, turretSize * 3 / 11, turretSize * 9 / 10)
    ctx.strokeRect(-turretSize * 3 / 22, 0, turretSize * 3 / 11, turretSize * 9 / 10)
    ctx.fill()
    ctx.stroke()
    ctx.setTransform(1, 0, 0, 1, 0, 0)

    //Draw the main body
    ctx.fillStyle = "rgb(120, 201, 44)"
    ctx.beginPath()
    ctx.arc(x, y, turretSize / 2, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()

    //Draw the name
    ctx.font = (turretSize * 3 / 11) + "px Arial"
    ctx.beginPath()
    ctx.fillText(name, x - (ctx.measureText(name).width / 2), y - turretSize * 27 / 22)
    ctx.fill()
  }

  //Draw a tree on the canvas
  def drawTree(pos: GameClientProtocol.Position): Unit = {
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

  def drawSnowball(pos: GameClientProtocol.Position, size: Double): Unit = {
    ctx.strokeStyle = "rgb(100, 100, 100)"
    ctx.fillStyle = "rgb(208, 242, 237)"
    ctx.beginPath()
    ctx.arc(pos.x, pos.y, size, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()
  }

  def drawBorder(top: GameClientProtocol.Position, bottom: GameClientProtocol.Position): Unit = {
    ctx.beginPath()
    ctx.moveTo(top.x, top.y)
    ctx.lineTo(top.x, bottom.y)
    ctx.lineTo(bottom.x, bottom.y)
    ctx.lineTo(bottom.x, top.y)
    ctx.closePath()
    ctx.stroke()
  }

  def centerObject(pos: GameClientProtocol.Position, me: GameClientProtocol.Position): GameClientProtocol.Position = {
    Position(pos.x - me.x + size.width / 2, pos.y - me.y + size.height * 2 / 3)
  }

  def screenPosition(pos: GameClientProtocol.Position, playField: GameClientProtocol.PlayField): GameClientProtocol.Position = {
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

    Position(x, y)
  }

  window.onresize = (_: UIEvent) => {
    size = Size(window.innerWidth, window.innerHeight)
    gameCanvas.width = size.width
    gameCanvas.height = size.height
    clearScreen()
  }
}
