package snowy.connection

import org.scalajs.dom._
import snowy.GameServerProtocol._
import snowy.client.ClientDraw.size
import snowy.client.Keys
import snowy.playfield.GameMotion.{LeftTurn, NoTurn, RightTurn}
import snowy.connection.GameState.gameTime

class OutboundEvents(sendMessage: (GameServerMessage) => Unit) {
  sealed trait FiringState
  case object Firing     extends FiringState
  case object NotFiring  extends FiringState
  case object AutoFiring extends FiringState

  var shooting: FiringState = NotFiring

  var turning: Option[Direction] = None
  var slowing: Boolean           = false
  var coasting: Boolean          = false
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
      case Keys.Up() =>
        sendMessage(Boost(gameTime))
      case Keys.TurretLeft() if !turret.contains(GoLeft) =>
        sendMessage(Stop(TurretRight, gameTime))
        sendMessage(Start(TurretLeft, gameTime))
        turret = Some(GoLeft)
      case Keys.TurretRight() if !turret.contains(GoRight) =>
        sendMessage(Stop(TurretLeft, gameTime))
        sendMessage(Start(TurretRight, gameTime))
        turret = Some(GoRight)
      case Keys.Shoot() => shootPressed()
      case Keys.AutoFire() if shooting == NotFiring =>
        sendMessage(Start(Shooting, gameTime))
        shooting = AutoFiring
      case Keys.AutoFire() if shooting == Firing =>
        shooting = AutoFiring
      case Keys.AutoFire() if shooting == AutoFiring =>
        sendMessage(Stop(Shooting, gameTime))
        shooting = NotFiring
      case _ =>
    }
  }

  window.addEventListener(
    "keyup", { event: KeyboardEvent =>
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
        case Keys.Shoot() =>
          sendMessage(Stop(Shooting, gameTime))
          shooting = NotFiring
        case _ =>
      }

      (event.keyCode, slowing) match {
        case (Keys.Down(), true) =>
          sendMessage(Stop(Slowing, gameTime))
          slowing = false
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
        case Keys.Shoot() => shootReleased()
        case _            =>
      }
    }
  )

  window.addEventListener(
    "mousemove", { e: MouseEvent =>
      val angle = -Math.atan2(e.clientX - window.innerWidth / 2, e.clientY - window.innerHeight / 2)

      if (math.abs(mouseDir - angle) > .05) {
        sendMessage(TurretAngle(angle))
        sendMessage(TargetAngle(angle))
        mouseDir = angle
      }
    },
    false
  )

  window.addEventListener(
    "mousedown", { e: MouseEvent =>
      e.button match {
        case 0 => sendMessage(Start(Shooting, gameTime))
        case 2 => sendMessage(Boost(gameTime))
      }
      e.preventDefault()
    },
    false
  )

  window.onmousedown = { _ =>
    shootPressed()
  }

  window.onmouseup = { _ =>
    shootReleased()
  }

  def shootReleased(): Unit = {
    if (shooting == Firing) {
      sendMessage(Stop(Shooting, gameTime))
      shooting = NotFiring
    }
  }

  def shootPressed(): Unit = {
    if (shooting == NotFiring) {
      sendMessage(Start(Shooting, gameTime))
      shooting = Firing
    }
  }
  window.addEventListener("contextmenu", { e: Event =>
    e.preventDefault()
  }, false)

  window.addEventListener("mouseup", { e: MouseEvent =>
    sendMessage(Stop(Shooting, gameTime))
    e.preventDefault()
  }, false)
}
