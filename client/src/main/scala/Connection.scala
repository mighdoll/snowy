import ClientDraw._
import ClientMain.Size
import GameClientProtocol._
import GameServerProtocol._
import org.scalajs.dom
import org.scalajs.dom._
import upickle.default._

class Connection(name: String, size: Size, ctx: dom.CanvasRenderingContext2D) {
  val socket = new WebSocket(s"ws://${window.location.host}/game")

  val connect = write(Join(name)) //document.getElementById("username").asInstanceOf[html.Input].value

  socket.onopen = { event: Event =>
    socket.send(connect)
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
        case playfield: PlayField => gPlayField = playfield
        case trees: Trees => gTrees = trees
      }
    } catch {
      case e: Exception =>
        console.log(s"unexpected message: $msg, ($e)")
    }
  }

  var gTrees = Trees(Vector())
  var gPlayField = PlayField(0, 0)

  //When the client receives the state of canvas, draw all sleds
  def receivedState(state: State): Unit = {
    clearScreen()

    drawState(state, gTrees, gPlayField)
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
  window.onkeyup = { event: Event =>
    val keyEvent = event.asInstanceOf[dom.KeyboardEvent].key
    if (turning.contains(Right) &&
      (keyEvent == "ArrowRight" || keyEvent == "d" || keyEvent == "D")) {
      turning = None
    }
    if (turning.contains(Left) &&
      (keyEvent == "ArrowLeft" || keyEvent == "a" || keyEvent == "A")) {
      turning = None
    }
  }
  window.onmousemove = { event: Event =>
    val e = event.asInstanceOf[dom.MouseEvent]
    val angle = -Math.atan2(e.clientX - size.width / 2, e.clientY - size.height / 3)
    socket.send(write(TurretAngle(angle)))
  }
}
