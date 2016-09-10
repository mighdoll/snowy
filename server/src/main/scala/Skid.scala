import GameConstants._
import math.Pi

object Skid {
  def skidTime(deltaSeconds: Double): Double = deltaSeconds * maxSkidTime

  /** run a function on a vector if the vector isn't zero */
  private def transformNonZero(speed:Vec2d)(fn: =>Vec2d):Vec2d= {
    speed.length match {
      case 0 => speed
      case _ => fn
    }
  }

  /** Return a rotated speed vector adjusted for skidding effect for a sled.
    * Quickly moving sleds continue in the same direction (as if they had momentum),
    * slower sleds change direction more swiftly.
    *
    * @param skidTime time since last game turn as a % of the maximum skid time */
  def skid(current:Vec2d, rotation:Double, skidTime: Double): Vec2d = transformNonZero(current){
    val speed = current.length
    val currentDirection = current.angle(Vec2d.unitUp)

    // the ski direction we'll actually aim for
    // subtract out any extra rotations since the skis go both ways
    val targetDirection  = {
      val delta = rotation - currentDirection
      val baseOffset = math.abs(delta)
      val rotations = math.floor((baseOffset + Pi / 2) / Pi) * Pi
      val offset = delta.signum * (baseOffset - rotations)
      currentDirection + offset
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

