package snowy.connection

import org.scalajs.dom._
import snowy.GameServerProtocol._
import snowy.client.ClientMain.{getHeight, getWidth}
import snowy.client.Keys
import snowy.playfield.GameMotion.{LeftTurn, NoTurn, RightTurn}

import scala.collection.mutable

class OutboundEvents(gameState: GameState, sendMessage: (GameServerMessage) => Unit) {
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
  import gameState.gameTime

  window.addEventListener(
    "keydown", { event: KeyboardEvent =>
      event.keyCode match {
        case Keys.Right() if !turning.contains(GoRight) =>
          gameState.startTurn(RightTurn)
          sendMessage(Stop(Left, gameTime))
          sendMessage(Start(Right, gameTime))
          turning = Some(GoRight)
        case Keys.Left() if !turning.contains(GoLeft) =>
          gameState.startTurn(LeftTurn)
          sendMessage(Stop(Right, gameTime))
          sendMessage(Start(Left, gameTime))
          turning = Some(GoLeft)
        case Keys.Down() if !slowing =>
          sendMessage(Start(Slowing, gameTime))
          slowing = true
        case Keys.Up() =>
          sendMessage(Boost(gameTime))
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
  )

  window.addEventListener(
    "keyup", { event: KeyboardEvent =>
      (event.keyCode, turning) match {
        case (Keys.Right(), Some(GoRight)) =>
          gameState.startTurn(NoTurn)
          sendMessage(Stop(Right, gameTime))
          turning = None
        case (Keys.Left(), Some(GoLeft)) =>
          gameState.startTurn(NoTurn)
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
      event.keyCode match {
        case Keys.Shoot() => shootReleased()
        case _            =>
      }
    }
  )

  window.addEventListener(
    "mousemove", { e: MouseEvent =>
      val angle = -math.atan2(e.clientX - getWidth / 2, e.clientY - getHeight / 2)

      if (math.abs(mouseDir - angle) > .05) {
        sendMessage(TargetAngle(angle))
        mouseDir = angle
      }
      // TODO replace magic numbers e.g. '20'
      // TODO make a function for this, e.g. 'mouseNearCenter'
      if (math.pow(e.clientX - getWidth / 2, 2) + math
            .pow(e.clientY - getHeight / 2, 2) < math.pow(40, 2)) {
        sendMessage(Start(Slowing, gameTime))
      } else {
        sendMessage(Stop(Slowing, gameTime))
      }
    }
  )

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

  private val down = mutable.Set[Char]()
  window.addEventListener(
    "keydown", { e: KeyboardEvent =>
      down += e.keyCode.toChar
      if (down.contains('Y')) {
        down.foreach {
          case key if key != 'Y' => sendMessage(DebugKey(key))
          case _                 =>
        }
      }
    }
  )
  window.addEventListener("keyup", (e: KeyboardEvent) => {
    down -= e.keyCode.toChar
  })

  window.addEventListener(
    "mousedown", { e: MouseEvent =>
      e.button match {
        case 0 => shootPressed()
        case 2 => sendMessage(Boost(gameTime))
      }
    }
  )

  window.addEventListener("mouseup", { e: MouseEvent =>
    shootReleased()
  })

  window.addEventListener("contextmenu", { e: Event =>
    e.preventDefault()
  })
}
