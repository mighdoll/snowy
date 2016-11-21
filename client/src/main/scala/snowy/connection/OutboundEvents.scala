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
  var slowing: Boolean           = false
  var pushing: Boolean           = false
  var turret: Option[Direction]  = None
  var mouseDir: Double           = 0

  sealed trait Direction

  object GoLeft  extends Direction
  object GoRight extends Direction

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
      case Keys.Down() if !slowing =>
        sendMessage(Start(Slowing, gameTime))
        slowing = true
      case Keys.Up() if !pushing =>
        sendMessage(Push(gameTime))
        pushing = true
      case Keys.TurretLeft() if !turret.contains(GoLeft) =>
        sendMessage(Stop(TurretRight, gameTime))
        sendMessage(Start(TurretLeft, gameTime))
        turret = Some(GoLeft)
      case Keys.TurretRight() if !turret.contains(GoRight) =>
        sendMessage(Stop(TurretLeft, gameTime))
        sendMessage(Start(TurretRight, gameTime))
        turret = Some(GoRight)
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
    (event.keyCode, slowing) match {
      case (Keys.Down(), true) =>
        sendMessage(Stop(Slowing, gameTime))
        slowing = false
      case _ =>
    }
    (event.keyCode, pushing) match {
      case (Keys.Up(), true) =>
        pushing = false
      case _ =>
    }
    (event.keyCode, turret) match {
      case (Keys.TurretLeft(), Some(GoLeft)) =>
        sendMessage(Stop(TurretLeft, gameTime))
        turret = None
      case (Keys.TurretRight(), Some(GoRight)) =>
        sendMessage(Stop(TurretRight, gameTime))
        turret = None
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
    sendMessage(Start(Shooting, gameTime))
  }
  window.onmouseup = { _: Event =>
    sendMessage(Stop(Shooting, gameTime))
  }
}
