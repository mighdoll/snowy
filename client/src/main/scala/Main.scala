import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom._
import upickle.default._
import GameServerProtocol._
import GameClientProtocol._

object ClientMain extends JSApp {

  case class sizeO(width: Int, height: Int)

  var size = sizeO(window.innerWidth, window.innerHeight)

  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  gameCanvas.width = size.width
  gameCanvas.height = size.height
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  var snowLoop: Option[Int] = None
  var gTrees = Trees(Vector(Tree(20, Position(446, 69))))
  var gPlayField = PlayField(0, 0)

  var time = 0

  def draw() = {
    //Simple drawing with time
    //TODO: Replace with snow
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()

    ctx.beginPath()
    ctx.moveTo(10, 10)
    ctx.lineTo(
      size.width - (time % size.width),
      size.height - (time % size.height)
    )
    ctx.stroke()

    time += 1
  }

  def connect() = {
    val socket = new WebSocket(s"ws://${window.location.host}/game")

    val join = Join(document.getElementById("username").asInstanceOf[html.Input].value)
    val msg = write(join)

    socket.onopen = { event: Event =>
      socket.send(msg)
    }
    socket.onerror = { event: ErrorEvent =>
      console.log(s"Failed: code: $event")
    }
    socket.onclose = { event: Event =>
      console.log(s"socket closed ")
    }

    socket.onmessage = { event: MessageEvent =>
      val msg = event.data.toString
      try {
        read[GameClientMessage](msg) match {
          case state: State => receivedState(state)
          case playfield: PlayField => receivedPlayField(playfield)
          case trees: Trees => receivedTrees(trees)
        }
      } catch {
        case e: Exception =>
          console.log(s"unexpected message: $msg, ($e)")
      }
    }

    sealed trait Direction
    object Left extends Direction
    object Right extends Direction
    var turning: Option[Direction] = None

    window.setInterval(() =>
      turning match {
        case Some(Left) => socket.send(write(TurnLeft))
        case Some(Right) => socket.send(write(TurnRight))
        case _ =>
      }, 20)

    window.onkeydown = { event: Event =>
      event.asInstanceOf[dom.KeyboardEvent].key match {
        case "ArrowRight" | "d" | "D" => turning = Some(Right)
        case "ArrowLeft" | "a" | "A" => turning = Some(Left)
        case _ =>
      }
    }
    window.onkeyup = { _: Event => turning = None }
    window.onmousemove = { event: Event =>
      val e = event.asInstanceOf[dom.MouseEvent]
      val angle = -Math.atan2(e.clientX - size.width / 2, e.clientY - size.height / 2)
      socket.send(write(TurretAngle(angle)))
    }
  }

  //When the users sends the login form, send it as a username to the server
  document.getElementById("login-form").asInstanceOf[html.Form].onsubmit = { event: Event =>
    //Connect to the WebSocket server
    connect()

    //Swap front and back panes
    document.getElementById("game-div").asInstanceOf[html.Div].className = "fullscreen"
    document.getElementById("start-div").asInstanceOf[html.Div].className = "back fullscreen"

    //Stop drawing the snow as a background
    snowLoop.foreach { id => window.clearInterval(id) }

    //Do not redirect
    false
  }

  def main(): Unit = {
    //Start the background loop
    snowLoop = Some(window.setInterval(draw _, 10))
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
    ctx.font = (turretSize * 3 / 11) + "px Arial";
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
    Position(pos.x - me.x + size.width / 2, pos.y - me.y + size.height / 2)
  }

  def addOffset(pos: GameClientProtocol.Position, offset: PlayField): GameClientProtocol.Position = {
    Position(pos.x + offset.width, pos.y + offset.height)
  }

  def drawState(state: State, offset: PlayField): Unit = {
    //Draw all snowballs
    state.snowballs.foreach { snowball =>
      drawSnowball(addOffset(centerObject(snowball.position, state.mySled.position), offset), 10.0)
    }
    //Draw all sleds
    drawSled("me", addOffset(Position(size.width / 2, size.height / 2), offset), state.mySled.turretRotation, state.mySled.rotation)
    state.sleds.foreach { sled =>
      drawSled(sled.userName, addOffset(centerObject(sled.position, state.mySled.position), offset), sled.turretRotation, sled.rotation)
    }

    gTrees.trees.foreach { tree =>
      drawTree(addOffset(centerObject(tree.position, state.mySled.position), offset))
    }

    drawBorder(addOffset(centerObject(GameClientProtocol.Position(0, 0), state.mySled.position), offset), addOffset(centerObject(GameClientProtocol.Position(gPlayField.width, gPlayField.height), state.mySled.position), offset))
  }

  //When the client receives the state of canvas, draw all sleds
  def receivedState(state: State): Unit = {
    //Clear the screen
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()

    drawState(state, PlayField(-gPlayField.width, -gPlayField.height))
    drawState(state, PlayField(-gPlayField.width, 0))
    drawState(state, PlayField(-gPlayField.width, gPlayField.height))
    drawState(state, PlayField(0, -gPlayField.height))
    drawState(state, PlayField(0, 0))
    drawState(state, PlayField(0, gPlayField.height))
    drawState(state, PlayField(gPlayField.width, -gPlayField.height))
    drawState(state, PlayField(gPlayField.width, 0))
    drawState(state, PlayField(gPlayField.width, gPlayField.height))
  }

  def receivedTrees(trees: Trees): Unit = {
    gTrees = trees
  }

  def receivedPlayField(playField: PlayField): Unit = {
    gPlayField = playField
  }

  window.onresize = (_: UIEvent) => {
    size = sizeO(window.innerWidth, window.innerHeight)
    gameCanvas.width = size.width
    gameCanvas.height = size.height
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()
  }
}
