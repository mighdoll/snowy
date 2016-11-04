package snowy.connection

import org.scalajs.dom._
import snowy.GameServerProtocol._
import snowy.client.ClientDraw.size
import snowy.client.Keys

class OutboundEvents(sendMessage: (GameServerMessage) => Unit) {

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
      case Some(GoLeft) => sendMessage(Start(Left))
      case Some(GoRight) => sendMessage(Start(Right))
      case _ =>
    }
    speeding match {
      case Some(SlowDown) => sendMessage(Start(Slow))
      case Some(SpeedUp) => sendMessage(Start(Push))
      case _ =>
    }
  }, 500)

  window.onkeydown = { event: KeyboardEvent =>
    event.key match {
      case Keys.Right() if !turning.contains(GoRight) =>
        sendMessage(Stop(Left))
        sendMessage(Start(Right))
        turning = Some(GoRight)
      case Keys.Left() if !turning.contains(GoLeft) =>
        sendMessage(Stop(Right))
        sendMessage(Start(Left))
        turning = Some(GoLeft)
      case Keys.Down() if !speeding.contains(SlowDown) =>
        sendMessage(Stop(Push))
        sendMessage(Start(Slow))
        speeding = Some(SlowDown)
      case Keys.Up() if !speeding.contains(SpeedUp) =>
        sendMessage(Stop(Slow))
        sendMessage(Start(Push))
        speeding = Some(SpeedUp)
      case _ =>
    }
  }

  window.onkeyup = { event: KeyboardEvent =>
    (event.key, turning) match {
      case (Keys.Right(), Some(GoRight)) =>
        sendMessage(Stop(Right))
        turning = None
      case (Keys.Left(), Some(GoLeft)) =>
        sendMessage(Stop(Left))
        turning = None
      case _ =>
    }
    (event.key, speeding) match {
      case (Keys.Up(), Some(SpeedUp)) =>
        sendMessage(Stop(Push))
        speeding = None
      case (Keys.Down(), Some(SlowDown)) =>
        sendMessage(Stop(Slow))
        speeding = None
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
