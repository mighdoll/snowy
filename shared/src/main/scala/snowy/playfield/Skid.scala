package snowy.playfield

import snowy.GameConstants._
import vector.Vec2d

import scala.math.Pi

object Skid {

  /** Return a rotated speed vector adjusted for skidding effect for a sled.
    * Quickly moving sleds continue in the same direction (as if they had momentum),
    * slower sleds change direction more swiftly.
    */
  def skid(current: Vec2d,
           rotation: Double,
           maxSpeed: Double,
           mass: Double,
           deltaSeconds: Double): Vec2d = {
    val skidTime = deltaSeconds * maxSkidTime

    current.transform {
      case _ if !current.zero =>
        val speed            = current.length
        val currentDirection = current.angle(Vec2d.unitUp)

        // the ski direction we'll actually aim for
        // subtract out any extra rotations since the skis go both ways
        val targetDirection = {
          val delta      = rotation - currentDirection
          val baseOffset = math.abs(delta)
          val rotations  = math.floor((baseOffset + Pi / 2) / Pi) * Pi
          rotation - delta.signum * rotations
        }

        // skidFactor of 1.0 means no more skidding, proceed directly to targetDirection
        val skidFactor = {
          val rawSkidFactor = maxSpeed * skidTime / (speed * mass)
          math.min(1.0, rawSkidFactor)
        }
        val newVector = {
          val newAngle = currentDirection + ((targetDirection - currentDirection) * skidFactor)
          Vec2d.fromRotation(newAngle) * speed
        }

        newVector
    }
  }
}
