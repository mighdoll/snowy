package snowy.connection

import org.scalajs.dom._
import snowy.GameServerProtocol._
import snowy.client.ClientDraw.size
import snowy.client.Keys
import snowy.playfield.GameMotion.{LeftTurn, NoTurn, RightTurn}
import snowy.connection.GameState.gameTime

class OutboundEvents(sendMessage: (GameServerMessage) => Unit) {

  var shooting: Boolean          = false
  var turning: Option[Direction] = None
  var speeding: Option[Speed]    = None
  var mouseDir: Double           = 0

  sealed trait Direction
  sealed trait Speed

  object GoLeft  extends Direction
  object GoRight extends Direction

  object SpeedUp  extends Speed
  object SlowDown extends Speed

  window.onkeydown = { event: KeyboardEvent =>
    event.keyCode match {
      case Keys.Right() if !turning.contains(GoRight) =>
        GameState.startTurn(RightTurn)
        sendMessage(Stop(Left, gameTime))
        sendMessage(Start(Right, gameTime))
        turning = Some(GoRight)
      case Keys.Left() if !turning.contains(GoLeft) =>
        GameState.startTurn(LeftTurn)
        sendMessage(Stop(Right, gameTime))
        sendMessage(Start(Left, gameTime))
        turning = Some(GoLeft)
      case Keys.Down() if !speeding.contains(SlowDown) =>
        sendMessage(Stop(Pushing, gameTime))
        sendMessage(Start(Slowing, gameTime))
        speeding = Some(SlowDown)
      case Keys.Up() if !speeding.contains(SpeedUp) =>
        sendMessage(Stop(Slowing, gameTime))
        sendMessage(Start(Pushing, gameTime))
        speeding = Some(SpeedUp)
      case Keys.Space() if !shooting =>
        sendMessage(Start(Shooting, gameTime))
        shooting = true
      case _ =>
    }
  }

  window.onkeyup = { event: KeyboardEvent =>
    (event.keyCode, turning) match {
      case (Keys.Right(), Some(GoRight)) =>
        GameState.startTurn(NoTurn)
        sendMessage(Stop(Right, gameTime))
        turning = None
      case (Keys.Left(), Some(GoLeft)) =>
        GameState.startTurn(NoTurn)
        sendMessage(Stop(Left, gameTime))
        turning = None
      case _ =>
    }
    (event.keyCode, speeding) match {
      case (Keys.Up(), Some(SpeedUp)) =>
        sendMessage(Stop(Pushing, gameTime))
        speeding = None
      case (Keys.Down(), Some(SlowDown)) =>
        sendMessage(Stop(Slowing, gameTime))
        speeding = None
      case _ =>
    }
    event.keyCode match {
      case Keys.Space() =>
        sendMessage(Stop(Shooting, gameTime))
        shooting = false
      case _ =>
    }
  }

  window.onmousemove = { e: MouseEvent =>
    val angle = -Math.atan2(e.clientX - size.x / 2, e.clientY - size.y / 2)

    if (math.abs(mouseDir - angle) > .05) {
      sendMessage(TurretAngle(angle))
      mouseDir = angle
    }
  }

  window.onmousedown = { _: Event =>
    sendMessage(Shoot(gameTime))
  }
}
