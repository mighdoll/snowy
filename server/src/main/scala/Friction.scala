import GameConstants.Friction._

object Friction {
  def apply(deltaSeconds: Double): Friction =
    new Friction(minFriction * deltaSeconds, maxFriction * deltaSeconds)
}

class Friction(frictionMin:Double, frictionMax:Double) {
  def apply(current: Vec2d, rotation: Double): Vec2d = {
    current.transform { case _ if !current.zero =>
      import math.{pow, abs, sin, max, min}
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
