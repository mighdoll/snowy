import ClientDraw._
import GameClientProtocol._
import GameServerProtocol._
import org.scalajs.dom
import org.scalajs.dom._
import upickle.default._

class Connection(name: String) {
  document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
  document.getElementById("start-div").asInstanceOf[html.Div].classList.add("hide")

  val socket = new WebSocket(s"ws://${window.location.host}/game")

  val connect = write(Join(name))

  socket.onopen = { event: Event =>
    socket.send(connect)
  }
  socket.onerror = { event: ErrorEvent =>
    console.log(s"Failed: code: $event")
  }
  socket.onclose = { event: Event =>
    console.log(s"socket closed ")

    document.getElementById("game-div").asInstanceOf[html.Div].classList.add("back")
    document.getElementById("start-div").asInstanceOf[html.Div].classList.remove("hide")
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

  object GoLeft extends Direction

  object GoRight extends Direction

  sealed trait Speed

  object SpeedUp extends Speed

  object SlowDown extends Speed

  var turning: Option[Direction] = None
  var speeding: Option[Speed] = None

  window.setInterval(() => {
    turning match {
      case Some(GoLeft) => socket.send(write(Start(Left)))
      case Some(GoRight) => socket.send(write(Start(Right)))
      case _ =>
    }
    speeding match {
      case Some(SlowDown) => socket.send(write(Start(Slow)))
      case Some(SpeedUp) => socket.send(write(Start(Push)))
      case _ =>
    }
  }, 10000)
  window.onkeydown = { event: Event =>
    event.asInstanceOf[dom.KeyboardEvent].key match {
      case "ArrowRight" | "d" | "D" =>
        socket.send(write(Stop(Left)))
        socket.send(write(Start(Right)))
        turning = Some(GoRight)
      case "ArrowLeft" | "a" | "A" =>
        socket.send(write(Stop(Right)))
        socket.send(write(Start(Left)))
        turning = Some(GoLeft)
      case "ArrowDown" | "s" | "S" =>
        socket.send(write(Stop(Push)))
        socket.send(write(Start(Slow)))
        speeding = Some(SlowDown)
      case "ArrowUp" | "w" | "W" =>
        socket.send(write(Stop(Slow)))
        socket.send(write(Start(Push)))
        speeding = Some(SpeedUp)
      case _ =>
    }
  }
  window.onkeyup = { event: Event =>
    val keyEvent = event.asInstanceOf[dom.KeyboardEvent].key
    if (turning.contains(GoRight) &&
      (keyEvent == "ArrowRight" || keyEvent == "d" || keyEvent == "D")) {
      socket.send(write(Stop(Right)))
      turning = None
    }
    if (turning.contains(GoLeft) &&
      (keyEvent == "ArrowLeft" || keyEvent == "a" || keyEvent == "A")) {
      socket.send(write(Stop(Left)))
      turning = None
    }
    if (turning.contains(SpeedUp) &&
      (keyEvent == "ArrowUp" || keyEvent == "w" || keyEvent == "W")) {
      socket.send(write(Stop(Push)))
      speeding = None
    }
    if (turning.contains(SlowDown) &&
      (keyEvent == "ArrowDown" || keyEvent == "s" || keyEvent == "S")) {
      socket.send(write(Stop(Slow)))
      speeding = None
    }
  }
  window.onmousemove = { event: Event =>
    val e = event.asInstanceOf[dom.MouseEvent]
    val angle = -Math.atan2(e.clientX - size.width / 2, e.clientY - size.height / 2)
    socket.send(write(TurretAngle(angle)))
  }
}
