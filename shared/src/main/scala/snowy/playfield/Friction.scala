package snowy.playfield

import scala.math._
import snowy.GameConstants.Friction._
import snowy.GameConstants.maxSpeed
import vector.Vec2d

object Friction {
  def apply(deltaSeconds: Double): Friction =
    new Friction(minFriction * deltaSeconds, maxFriction * deltaSeconds)
}

/** a slowdown applied against the direction of travel */
class Friction(frictionMin: Double, frictionMax: Double) {
  def apply(current: Vec2d, rotation: Double): Vec2d = {
    current.transform { case _ if !current.zero =>
      // friction is min when skis are aligned with direction, max when skis are at 90 degrees
      val speed = current.length
      val direction = current.unit
      val rotationVector = Vec2d.fromRotation(rotation)
      val angleSkiToTravel = direction.angle(rotationVector)
      val rotationFactor = pow(abs(sin(angleSkiToTravel)), brakeSteepness)

      // -speed in direction of travel
      val sledFriction = frictionMax * rotationFactor

      val adjustedFriction = min(speed, max(frictionMin, sledFriction))
      val newSpeed = direction * (speed - adjustedFriction)
      newSpeed
    }
  }
}

/** a force applied directly in the current direction of travel */
class InlineForce(force: Double) {
  def apply(current: Vec2d): Vec2d = {
    current.transform { case _ if !current.zero =>
      val speed = min(max(current.length + force, 0), maxSpeed)
      current.unit * speed
    }
  }
}

