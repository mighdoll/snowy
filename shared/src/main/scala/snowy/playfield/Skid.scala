package snowy.playfield

import scala.math.Pi
import snowy.GameConstants._
import vector.Vec2d

object Skid {
  def apply(deltaSeconds: Double): Skid =
    new Skid(deltaSeconds * maxSkidTime)
}

/** Rotate speed vectors to take into account skidding effect
  *
  * @param skidTime time since last game turn as a % of the maximum skid time */
class Skid(skidTime: Double) {

  /** Return a rotated speed vector adjusted for skidding effect for a sled.
    * Quickly moving sleds continue in the same direction (as if they had momentum),
    * slower sleds change direction more swiftly.
    */
  def apply(current: Vec2d, rotation: Double, maxSpeed: Double): Vec2d = {
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
          val rawSkidFactor = maxSpeed * skidTime / speed
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
