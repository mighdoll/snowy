package snowy.server

import snowy.GameServerProtocol.{
  DriveControl,
  Left,
  PersistentControl,
  Right,
  Shooting,
  Slowing,
  StartStopControl
}
import snowy.playfield.GameMotion.{LeftTurn, RightTurn}
import snowy.playfield._
import socketserve.ClientId
import vector.Vec2d

/** Support sled controls that have an ongoing. e.g. autofire, turning, etc. */
class PersistentControls(gameStateImplicits: GameStateImplicits) {
  import gameStateImplicits._
  val pendingControls = new PendingControls

  /** client has started to operate a sled control. e.g. shooting, braking */
  def startControl(id: ClientId, cmd: StartStopControl, time: Long): Unit = {
    cmd match {
      case persistentControl: PersistentControl =>
        pendingControls.startCommand(id, persistentControl, time)
      case driveControl: DriveControl =>
        for (sled <- id.sled) {
          driveControl match {
            case Slowing => sled.driveMode.driveMode(SledDrive.Braking)
          }
        }
    }
  }

  /** client has stopped a sled control. e.g. shooting, braking */
  def stopControl(id: ClientId, cmd: StartStopControl, time: Long): Unit = {
    cmd match {
      case persistentControl: PersistentControl =>
        pendingControls.stopCommand(id, persistentControl, time)
      case _: DriveControl =>
        for (sled <- id.sled) {
          sled.driveMode.driveMode(SledDrive.Driving)
        }
    }
  }

  /** apply any pending but not yet cancelled commands from user actions,
    * e.g. turning or slowing */
  def applyCommands(motion: GameMotion,
                    snowballs: Snowballs,
                    gameTime: Long,
                    deltaSeconds: Double): Unit = {
    pendingControls.foreachCommand { (id, command, time) =>
      id.sled.foreach { sled =>
        command match {
          case Left     => motion.turnSled(sled, LeftTurn, deltaSeconds)
          case Right    => motion.turnSled(sled, RightTurn, deltaSeconds)
          case Shooting => shootSnowball(sled, snowballs, gameTime)
        }
      }
    }
  }

  def shootSnowball(sled: Sled, snowballs: Snowballs, gameTime: Long)(
        implicit snowballTracker: PlayfieldTracker[Snowball]
  ): Unit = {
    if (sled.lastShotTime + sled.minRechargeTime < gameTime) {
      val launchDistance = sled.bulletLaunchPosition.length + sled.radius
      val launchPos = sled.bulletLaunchPosition
        .rotate(sled.rotation)
        .unit * launchDistance
      val direction = Vec2d.fromRotation(sled.rotation)
      val ball = Snowball(
        ownerId = sled.id,
        speed = sled.speed + (direction * sled.bulletSpeed),
        radius = sled.bulletRadius,
        mass = sled.bulletMass,
        spawned = gameTime,
        impactDamage = sled.bulletImpactFactor,
        health = sled.bulletHealth,
        lifetime = sled.bulletLifetime
      )
      snowballs.addBall(ball, sled.position + launchPos)

      val recoilForce = direction * -sled.bulletRecoil
      sled.speed = sled.speed + recoilForce
      sled.lastShotTime = gameTime
    }
  }

}
