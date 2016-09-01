import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom._
import upickle.default._
import GameServerProtocol._
import GameClientProtocol._

object TryMe extends JSApp {
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  gameCanvas.width = size.width
  gameCanvas.height = size.height
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  var snowLoop: Option[Int] = None
  var gTrees = Trees(Vector(Tree(20, Position(446, 69))))

  object size {
    val width = window.innerWidth
    val height = window.innerHeight
  }

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
    val socket = new WebSocket("ws://localhost:2345/game")

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

    window.onkeydown = { event: Event =>
      event.asInstanceOf[dom.KeyboardEvent].key match {
        case "ArrowRight" | "d" | "D" => socket.send(write(TurnRight))
        case "ArrowLeft" | "a" | "A" => socket.send(write(TurnLeft))
        case _ =>
      }
    }
    window.onmousemove = { event: Event =>
      val e = event.asInstanceOf[dom.MouseEvent]
      socket.send(write(Mouse(GameClientProtocol.Position(e.clientX.toInt, e.clientY.toInt))))
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
    ctx.font = (turretSize * 2 / 11) + "px Arial";
    ctx.beginPath()
    ctx.fillText(name, x - (ctx.measureText(name).width / 2), y - turretSize * 27 / 22)
    ctx.fill()
  }

  //Draw a tree on the canvas
  def drawTree(pos: GameClientProtocol.Position): Unit = {
    ctx.fillStyle = "rgb(30, 184, 22)"
    ctx.beginPath()
    ctx.fillRect(pos.x, pos.y, 10, 10)
    ctx.fill()
  }

  def drawSnowball(pos: GameClientProtocol.Position, size: Double): Unit = {
    ctx.strokeStyle = "rgb(100, 100, 100)"
    ctx.fillStyle = "rgb(208, 242, 237)"
    ctx.beginPath()
    ctx.arc(pos.x, pos.y, size, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()
  }

  //When the client receives the state of canvas, draw all sleds
  def receivedState(state: State): Unit = {
    //Clear screen
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()

    //Draw all snowballs
    state.snowballs.foreach { snowball =>
      drawSnowball(snowball.position, 10.0)
    }
    //Draw all sleds
    state.sleds.foreach { sled =>
      drawSled(sled.userName, sled.position, sled.turretRotation, sled.rotation)
    }

    gTrees.trees.foreach { tree =>
      drawTree(tree.position)
    }
  }

  //When the client receives all trees, draw them
  def receivedTrees(trees: Trees): Unit = {
    gTrees = trees
  }

  def receivedPlayField(playField: PlayField): Unit = {
    console.log(s"received playField: $playField")
  }
}
