package snowy.playfield

import scala.collection.mutable
import snowy.Awards.Travel
import snowy.GameConstants.playfield
import vector.Vec2d
import Friction.friction

/** Moving objects in each game time slice */
object GameMotion {

  /** update sleds and snowballs speeds and positions */
  def moveSleds(sleds: Store[Sled], deltaSeconds: Double): (Store[Sled], Seq[Travel]) = {
    val newSleds = updateSledSpeedVector(sleds, deltaSeconds)
    repositionSleds(newSleds, deltaSeconds)
  }

  /** move snowballs to their new location for this time period */
  def moveSnowballs(snowballs: Store[Snowball], deltaSeconds: Double): Store[Snowball] = {
    snowballs.replaceItems { snowball =>
      val deltaPos = snowball.speed * deltaSeconds
      snowball.copy(pos = wrapInPlayfield(snowball.pos + deltaPos))
    }
  }

  /** Constrain a value between 0 and a max value.
    * values past one border of the range are wrapped to the other side
    *
    * @return the wrapped value */
  def wrapBorder(value: Double, max: Double): Double = {
    val result =
      if (value >= max * 2.0)
        max
      else if (value >= max)
        value - max
      else if (value < -max)
        0
      else if (value < 0)
        max + value
      else
        value
    if (result < 0 || result > max) {
      println(s"wrapBorder error: wrap $value  between (0 < $max) = $result")
    }
    result
  }

  /** constrain a position to be within the playfield */
  def wrapInPlayfield(pos: Vec2d): Vec2d = {
    Vec2d(
      wrapBorder(pos.x, playfield.x),
      wrapBorder(pos.y, playfield.y)
    )
  }

  /** Update the direction and velocity of all sleds based on gravity and friction */
  private def updateSledSpeedVector(sleds: Store[Sled],
                                    deltaSeconds: Double): Store[Sled] = {
    sleds.replaceItems { sled =>
      val skid     = Skid(deltaSeconds)

      import sled.rotation
      val gravity       = Gravity(deltaSeconds, sled.gravity)
      val gravitySpeed  = gravity(sled.speed, rotation, sled.maxSpeed)
      val skidSpeed     = skid(gravitySpeed, rotation, sled.maxSpeed)
      val frictionSpeed = friction(skidSpeed, rotation, deltaSeconds, sled.mass)

      sled.copy(speed = frictionSpeed)
    }
  }

  /** move the sleds to their new location for this time period */
  private def repositionSleds(sleds: Store[Sled],
                              deltaSeconds: Double): (Store[Sled], Seq[Travel]) = {
    val awards = mutable.ListBuffer[Travel]()
    val newSleds =
      sleds.replaceItems { sled =>
        val positionChange = sled.speed * deltaSeconds
        val moved          = sled.pos + positionChange
        val wrapped        = wrapInPlayfield(moved)
        val distance       = positionChange.length
        if (distance > 0) awards += Travel(sled.id, distance)
        sled.copy(pos = wrapped)
      }
    (newSleds, awards.toList)
  }
}
