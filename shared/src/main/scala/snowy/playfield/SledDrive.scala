package snowy.playfield
import snowy.GameConstants.Friction.slowButtonFriction
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
    sled.speed.transform {
      case speed if !speed.zero =>
        val speedLength = (speed.length + acceleration).clip(0, sled.maxSpeed)
        speed.unit * speedLength
    }
  }
}

import SledDrive._

class SledDrive {

  private var drive: Drive = Driving

  def driveSled(sled: Sled, deltaSeconds: Double): Unit = {
    drive match {
      case Coasting => // nothing to do
      case Driving  => accelerate(sled, sled.driveAcceleration * deltaSeconds)
      case Braking  => brake(sled, -slowButtonFriction * deltaSeconds / sled.mass)
    }
  }

  def driveMode(mode: Drive): Unit = {
    drive = mode
  }

}
