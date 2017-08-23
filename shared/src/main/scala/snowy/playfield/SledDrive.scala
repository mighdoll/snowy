package snowy.playfield
import snowy.util.DoubleUtil._
import vector.Vec2d

object SledDrive {
  sealed trait Drive
  case object Driving extends Drive
  case object Braking extends Drive

  /** accelerate the sled along its current ski orientation */
  def accelerate(sled: Sled, acceleration: Double, gameTime: Long): Unit = {
    val newSpeed = sled.speed + (Vec2d.fromRotation(sled.rotation) * acceleration)
    val maxSpeed = sled.currentMaxSpeed(gameTime)
    if (newSpeed.length <= maxSpeed) sled.speed = newSpeed.clipLength(maxSpeed)
    sled.speed = newSpeed * sled.drag
  }

  /** brake counter the current direction of travel */
  def brake(sled: Sled, acceleration: Double, gameTime: Long): Unit = {
    sled.speed = sled.speed.transform {
      case speed if !speed.zero =>
        val maxSpeed    = sled.currentMaxSpeed(gameTime)
        val speedLength = (speed.length - acceleration).clip(0, maxSpeed)
        speed.unit * speedLength
    }
  }
}

import snowy.playfield.SledDrive._

/** driving mode of the sled: coasting, driving, or braking */
class SledDrive {

  private var drive: Drive = Driving

  /** accelerate or decelerate the sled based on the driving mode */
  def driveSled(sled: Sled, deltaSeconds: Double, gameTime: Long): Unit = {
    drive match {
      case Driving =>
        accelerate(sled, sled.driveAcceleration * deltaSeconds, gameTime)
      case Braking =>
        brake(sled, sled.brakeAcceleration * deltaSeconds / sled.mass, gameTime)
    }
  }

  /** set the driving mode of the sled */
  def driveMode(mode: Drive): Unit = {
    drive = mode
  }

}
