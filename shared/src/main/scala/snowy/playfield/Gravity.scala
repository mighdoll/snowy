package snowy.playfield

import vector.Vec2d

object Gravity {
  def apply(deltaSeconds: Double, acceleration: Double): Gravity =
    new Gravity(acceleration * deltaSeconds)
}

/** adjust speed based on acceleration from the force of gravity */
class Gravity(gravityForce: Double) {

  import math.cos

  /** adjust speed based on acceleration from the force of gravity */
  def apply(speed: Vec2d, rotation: Double, maxSpeed: Double): Vec2d = {
    val gravityLength = cos(rotation) * gravityForce // +speed in direction of travel
    val gravitySpeedV = Vec2d.fromRotation(rotation) * gravityLength
    val newSpeed      = speed + gravitySpeedV

    if (newSpeed.length <= maxSpeed) newSpeed
    else newSpeed.unit * maxSpeed
  }
}
