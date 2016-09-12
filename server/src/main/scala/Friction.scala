import GameConstants.Friction._

object Friction {

  case class Extent(min: Double, max: Double)

  def frictionForce(deltaSeconds: Double): Extent =
    Extent(minFriction * deltaSeconds, maxFriction * deltaSeconds)

  def applyFriction(current: Vec2d, rotation: Double, friction: Extent): Vec2d = {
    current.transform { case _ if !current.zero =>
      import math._
      // friction is min when skis are aligned with direction, max when skis are at 90 degrees
      val speed = current.length
      val direction = current.unit
      val rotationVector = Vec2d.fromRotation(rotation)
      val angleSkiToTravel = direction.angle(rotationVector)
      val rotationFactor = pow(abs(sin(angleSkiToTravel)), brakeSteepness)

      // -speed in direction of travel
      val sledFriction = friction.max * rotationFactor

      val adjustedFriction = min(speed, max(friction.min, sledFriction))
      val newSpeed = direction * (speed - adjustedFriction)
      newSpeed
    }
  }
}
