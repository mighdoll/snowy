import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom._
import upickle.default._
import GameServerProtocol._
import GameClientProtocol._

object ClientMain extends JSApp {

  case class Size(width: Int, height: Int)

  var size = Size(window.innerWidth, window.innerHeight)

  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  gameCanvas.width = size.width
  gameCanvas.height = size.height
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  val draws = new ClientDraw(ctx, size)

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

  //When the client receives the state of canvas, draw all sleds
  def receivedState(state: State): Unit = {
    //Clear the screen
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()

    draws.drawState(state, PlayField(-gPlayField.width, -gPlayField.height), gTrees, gPlayField)
    draws.drawState(state, PlayField(-gPlayField.width, 0), gTrees, gPlayField)
    draws.drawState(state, PlayField(-gPlayField.width, gPlayField.height), gTrees, gPlayField)
    draws.drawState(state, PlayField(0, -gPlayField.height), gTrees, gPlayField)
    draws.drawState(state, PlayField(0, 0), gTrees, gPlayField)
    draws.drawState(state, PlayField(0, gPlayField.height), gTrees, gPlayField)
    draws.drawState(state, PlayField(gPlayField.width, -gPlayField.height), gTrees, gPlayField)
    draws.drawState(state, PlayField(gPlayField.width, 0), gTrees, gPlayField)
    draws.drawState(state, PlayField(gPlayField.width, gPlayField.height), gTrees, gPlayField)
  }

  def receivedTrees(trees: Trees): Unit = {
    gTrees = trees
  }

  def receivedPlayField(playField: PlayField): Unit = {
    gPlayField = playField
  }

  window.onresize = (_: UIEvent) => {
    size = Size(window.innerWidth, window.innerHeight)
    gameCanvas.width = size.width
    gameCanvas.height = size.height
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()
  }
}
