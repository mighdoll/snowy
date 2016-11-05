package snowy.connection

import org.scalajs.dom._
import snowy.GameServerProtocol._
import snowy.client.ClientDraw.size
import snowy.client.Keys

class OutboundEvents(sendMessage: (GameServerMessage) => Unit) {

  var shooting: Boolean = false
  var turning: Option[Direction] = None
  var speeding: Option[Speed]    = None

  sealed trait Direction
  sealed trait Speed

  object GoLeft  extends Direction
  object GoRight extends Direction

  object SpeedUp  extends Speed
  object SlowDown extends Speed

  window.onkeydown = { event: KeyboardEvent =>
    event.keyCode match {
      case Keys.Right() if !turning.contains(GoRight) =>
        sendMessage(Stop(Left))
        sendMessage(Start(Right))
        turning = Some(GoRight)
      case Keys.Left() if !turning.contains(GoLeft) =>
        sendMessage(Stop(Right))
        sendMessage(Start(Left))
        turning = Some(GoLeft)
      case Keys.Down() if !speeding.contains(SlowDown) =>
        sendMessage(Stop(Pushing))
        sendMessage(Start(Slowing))
        speeding = Some(SlowDown)
      case Keys.Up() if !speeding.contains(SpeedUp) =>
        sendMessage(Stop(Slowing))
        sendMessage(Start(Pushing))
        speeding = Some(SpeedUp)
      case Keys.Space() if !shooting =>
        sendMessage(Start(Shooting))
        shooting = true
      case _ =>
    }
  }

  window.onkeyup = { event: KeyboardEvent =>
    (event.keyCode, turning) match {
      case (Keys.Right(), Some(GoRight)) =>
        sendMessage(Stop(Right))
        turning = None
      case (Keys.Left(), Some(GoLeft)) =>
        sendMessage(Stop(Left))
        turning = None
      case _ =>
    }
    (event.keyCode, speeding) match {
      case (Keys.Up(), Some(SpeedUp)) =>
        sendMessage(Stop(Pushing))
        speeding = None
      case (Keys.Down(), Some(SlowDown)) =>
        sendMessage(Stop(Slowing))
        speeding = None
      case _ =>
    }
    event.keyCode match {
      case Keys.Space() =>
        sendMessage(Stop(Shooting))
        shooting = false
      case _ =>
    }
  }

  window.onmousemove = { e: MouseEvent =>
    val angle = -Math.atan2(e.clientX - size.x / 2, e.clientY - size.y / 2)
    sendMessage(TurretAngle(angle))
  }

  window.onmousedown = { _: Event =>
    sendMessage(Shoot)
  }
}
