import GameConstants._

/** Moving objects in each game time slice */
object GameMotion {
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

  /** update sleds and snowballs speeds and positions */
  def moveSleds(sleds:Store[Sled], deltaSeconds: Double): Store[Sled] = {
    val newSleds = updateSledSpeedVector(sleds, deltaSeconds)
    repositionSleds(newSleds, deltaSeconds)
  }

  /** move snowballs to their new location for this time period */
  def moveSnowballs(snowballs:Store[Snowball], deltaSeconds: Double): Store[Snowball] = {
    snowballs.replaceItems{snowball =>
      snowball.copy(pos = wrapInPlayfield(snowball.pos + snowball.speed))
    }
  }

  /** Update the direction and velocity of all sleds based on gravity and friction */
  private def updateSledSpeedVector(sleds:Store[Sled], deltaSeconds: Double): Store[Sled] = {
    val gravity = Gravity(deltaSeconds)
    val skid = Skid(deltaSeconds)
    val friction = Friction(deltaSeconds)
    sleds.replaceItems{ sled =>
      import sled.rotation
      val gravitySpeed = gravity(sled.speed, rotation)
      val skidSpeed = skid(gravitySpeed, rotation)
      val frictionSpeed = friction(skidSpeed, rotation)

      sled.copy(speed = frictionSpeed)
    }
  }

  /** move the sleds to their new location for this time period */
  private def repositionSleds(sleds:Store[Sled], deltaSeconds: Double): Store[Sled] = {
    sleds.replaceItems { sled =>
      val positionChange = sled.speed * deltaSeconds
      val moved = sled.pos + positionChange
      val wrapped = wrapInPlayfield(moved)
      val distance = positionChange.length
      sled.copy(pos = wrapped, distanceTraveled = distance)
    }
  }


}

