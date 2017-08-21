package snowy.playfield

import snowy.GameConstants.Friction._
import vector.Vec2d

import scala.math._

object Friction {

  /** @return speed after slowdown applied against the direction of travel */
  def friction(current: Vec2d,
               rotation: Double,
               deltaSeconds: Double,
               mass: Double): Vec2d = {
    val frictionMin = minFriction * deltaSeconds
    val frictionMax = maxFriction * deltaSeconds

    current.transform {
      case _ if !current.zero =>
        // friction is min when skis are aligned with direction, max when skis are at 90 degrees
        val speed     = current.length
        val direction = current.unit
        val rotationFactor = {
          val rotationVector   = Vec2d.fromRotation(rotation)
          val angleSkiToTravel = direction.angle(rotationVector)
          abs(sin(angleSkiToTravel))
        }

        // -speed in direction of travel
        val sledFriction = frictionMax * rotationFactor / mass

        val adjustedFriction = min(speed, max(frictionMin, sledFriction))
        val newSpeed         = direction * (speed - adjustedFriction)
        newSpeed
    }
  }
}
