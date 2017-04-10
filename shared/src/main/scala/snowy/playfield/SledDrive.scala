package snowy.playfield
import vector.Vec2d
import snowy.util.DoubleUtil._

object SledDrive {
  sealed trait Drive
  case object Driving  extends Drive
  case object Coasting extends Drive
  case object Braking  extends Drive

  /** accelerate the sled along its current ski orientation */
  def accelerate(sled: Sled, acceleration: Double): Unit = {
    val newSpeed = sled.speed + (Vec2d.fromRotation(sled.rotation) * acceleration)
    sled.speed = newSpeed.clipLength(sled.maxSpeed)
  }

  /** brake counter the current direction of travel */
  def brake(sled: Sled, acceleration: Double): Unit = {
    sled.speed = sled.speed.transform {
      case speed if !speed.zero =>
        val speedLength = (speed.length - acceleration).clip(0, sled.maxSpeed)
        speed.unit * speedLength
    }
  }
}

import SledDrive._

/** driving mode of the sled: coasting, driving, or braking */
class SledDrive {

  private var drive: Drive = Driving

  /** accelerate or decelerate the sled based on the driving mode */
  def driveSled(sled: Sled, deltaSeconds: Double): Unit = {
    drive match {
      case Coasting => // nothing to do
      case Driving  => accelerate(sled, sled.driveAcceleration * deltaSeconds)
      case Braking  => brake(sled, sled.brakeAcceleration * deltaSeconds / sled.mass)
    }
  }

  /** set the driving mode of the sled */
  def driveMode(mode: Drive): Unit = {
    drive = mode
  }

}
