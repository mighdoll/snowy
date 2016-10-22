package snowy.connection

import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameServerProtocol._
import snowy.client.ClientDraw.size
import snowy.client.Keys
import upickle.default._

class OutboundEvents(socket: NetworkSocket) {

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
      case Keys.Right() if !turning.contains(GoRight)  =>
        socket.send(write(Stop(Left)))
        socket.send(write(Start(Right)))
        turning = Some(GoRight)
      case Keys.Left() if !turning.contains(GoLeft)    =>
        socket.send(write(Stop(Right)))
        socket.send(write(Start(Left)))
        turning = Some(GoLeft)
      case Keys.Down() if !speeding.contains(SlowDown) =>
        socket.send(write(Stop(Push)))
        socket.send(write(Start(Slow)))
        speeding = Some(SlowDown)
      case Keys.Up() if !speeding.contains(SpeedUp)    =>
        socket.send(write(Stop(Slow)))
        socket.send(write(Start(Push)))
        speeding = Some(SpeedUp)
      case _                                           =>
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
    val angle = -Math.atan2(e.clientX - size.x / 2, e.clientY - size.y / 2)
    socket.send(write(TurretAngle(angle)))
  }

  window.onmousedown = { _: Event =>
    socket.send(write(Shoot))
  }
}
