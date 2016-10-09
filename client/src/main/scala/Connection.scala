import scala.concurrent.duration._
import ClientDraw._
import GameClientProtocol._
import GameServerProtocol._
import org.scalajs.dom._
import upickle.default._

class Connection(name: String) {
  document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
  document.getElementById("start-div").asInstanceOf[html.Div].classList.add("hide")

  val socket = {
    val inDelay = 50 milliseconds
    val outDelay = 50 milliseconds
    val url = s"ws://${window.location.host}/game"
    new NetworkSocket(url, inDelay, outDelay)
  }

  val connect = write(Join(name))

  socket.onOpen { _ =>
    socket.send(connect)
  }

  socket.onError { event =>
    console.log(s"Failed: code: $event")
  }

  socket.onClose { _ =>
    console.log(s"socket closed ")

    document.getElementById("game-div").asInstanceOf[html.Div].classList.add("back")
    document.getElementById("start-div").asInstanceOf[html.Div].classList.remove("hide")
  }

  socket.onMessage { event =>
    val msg = event.data.toString
    try {
      read[GameClientMessage](msg) match {
        case state: State                => receivedState(state)
        case playfield: Playfield        => gPlayField = playfield
        case trees: Trees                => gTrees = trees
        case Died                        => console.log("ToDo: sled's dead, deal with it.")
        case Ping                        => socket.send(write(Pong))
        case GameTime(time, oneWayDelay) => console.log(s"Game Time: $time, $oneWayDelay")
        case scoreboard: Scoreboard      => // console.log(s"scoreboard: $scoreboard")
      }
    } catch {
      case e: Exception =>
        console.log(s"unexpected message: $msg, ($e)")
    }
  }

  var gTrees = Trees(Vector())
  var gPlayField = Playfield(0, 0)

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
      case Some(GoLeft)  => socket.send(write(Start(Left)))
      case Some(GoRight) => socket.send(write(Start(Right)))
      case _             =>
    }
    speeding match {
      case Some(SlowDown) => socket.send(write(Start(Slow)))
      case Some(SpeedUp)  => socket.send(write(Start(Push)))
      case _              =>
    }
  }, 500)

  window.onkeydown = { event: KeyboardEvent =>
    event.key match {
      case Keys.Right() if turning != Some(GoRight)  =>
        socket.send(write(Stop(Left)))
        socket.send(write(Start(Right)))
        turning = Some(GoRight)
      case Keys.Left() if turning != Some(GoLeft)    =>
        socket.send(write(Stop(Right)))
        socket.send(write(Start(Left)))
        turning = Some(GoLeft)
      case Keys.Down() if speeding != Some(SlowDown) =>
        socket.send(write(Stop(Push)))
        socket.send(write(Start(Slow)))
        speeding = Some(SlowDown)
      case Keys.Up() if speeding != Some(SpeedUp)    =>
        socket.send(write(Stop(Slow)))
        socket.send(write(Start(Push)))
        speeding = Some(SpeedUp)
      case _                                         =>
    }
  }

  window.onkeyup = { event: KeyboardEvent =>
    (event.key, turning) match {
      case (Keys.Right(), Some(GoRight)) =>
        socket.send(write(Stop(Right)))
        turning = None
      case (Keys.Left(), Some(GoLeft))   =>
        socket.send(write(Stop(Left)))
        turning = None
      case _                             =>
    }
    (event.key, speeding) match {
      case (Keys.Up(), Some(SpeedUp))    =>
        socket.send(write(Stop(Push)))
        speeding = None
      case (Keys.Down(), Some(SlowDown)) =>
        socket.send(write(Stop(Slow)))
        speeding = None
      case _                             =>
    }
  }

  window.onmousemove = { e: MouseEvent =>
    val angle = -Math.atan2(e.clientX - size.width / 2, e.clientY - size.height / 2)
    socket.send(write(TurretAngle(angle)))
  }

  window.onmousedown = { _: Event =>
    socket.send(write(Shoot))
  }
}
