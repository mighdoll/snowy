package snowy.playfield

import vector.Vec2d

import scala.math.cos

object Gravity {

  /** adjust speed based on acceleration from the force of gravity */
  def gravity(speed: Vec2d,
              rotation: Double,
              maxSpeed: Double,
              acceleration: Double,
              deltaSeconds: Double): Vec2d = {

    val gravityForce  = acceleration * deltaSeconds
    val gravityLength = cos(rotation) * gravityForce // +speed in direction of travel
    val gravitySpeedV = Vec2d.fromRotation(rotation) * gravityLength
    val newSpeed      = speed + gravitySpeedV

    if (newSpeed.length <= maxSpeed) newSpeed
    else newSpeed.unit * maxSpeed
  }
}
