/** Moving objects in each game time slice */
trait GameMotion {
  self: GameControl =>

  /** update sleds and snowballs speeds and positions */
  protected def moveStuff(deltaSeconds: Double): Unit = {
    applyGravity(deltaSeconds)
    skidToRotate(deltaSeconds)
    frictionSlow(deltaSeconds)
    moveObjects(deltaSeconds)
    checkCollisions()
  }

  /** Increase the speed of sleds due to gravity */
  private def applyGravity(deltaSeconds: Double): Unit = {
    val gravity = 100.0 // pixels / second / second
    val gravityFactor = gravity * deltaSeconds
    mapSleds { sled =>
      val gravityLength = math.cos(sled.rotation) * gravityFactor // +speed in direction of travel
      val gravitySpeed = Vec2d.fromRotation(sled.rotation) * gravityLength
      val newSpeed = (sled.speed + gravitySpeed).min(maxSpeed)
      sled.copy(speed = newSpeed)
    }
  }

  /** Slow sleds based on friction */
  private def frictionSlow(deltaSeconds:Double): Unit =  {
    val friction = 100.0 // pixels / second / second
    val frictionFactor = friction * deltaSeconds
    mapSleds { sled =>
      import math._
      // friction is 0 when skis are aligned with direction, max when skis are at 90 degrees
      val direction = sled.speed.unit
      val rotation = Vec2d.fromRotation(sled.rotation)
      val angleSkiToTravel = direction.angle(rotation)
      val brakeSteepness = 4  // higher means braking effect curve peaks more narrowly (when skis are near 90 to travel)
      val rotationFactor = pow(sin(angleSkiToTravel), brakeSteepness)
      val sledFriction = frictionFactor * rotationFactor // -speed in direction of travel

      val speed = sled.speed.length
      val adjustedFriction = if (sledFriction < speed) sledFriction else speed
      val newSpeed = direction * (speed - adjustedFriction)
      sled.copy(speed = newSpeed)
    }
  }

  /** Adjust the speed based on the current rotation */
  private def skidToRotate(deltaSeconds: Double): Unit = {
    val skidSpeed = .3  // seconds to complete a skid
    val skidFactor = math.min(1.0, deltaSeconds / skidSpeed)
    mapSleds { sled =>
      val speed = sled.speed.length
      val current = sled.speed / speed
      val target = Vec2d(
        x = math.sin(sled.rotation),
        y = math.cos(sled.rotation)
      )
      val skidVector = current + ((target - current) * skidFactor)
      val newSpeed = skidVector * speed
      sled.copy(speed = newSpeed)
    }
  }

  /** Run a function that replaces each sled */
  private def mapSleds(fn: SledState => SledState): Unit = {
    sleds = sleds.map { case (id, sled) =>
      id -> fn(sled)
    }
  }

  /** Constrain a value between 0 and a max value.
    * values past one border of the range are wrapped to the other side
    *
    * @return the wrapped value */
  private def wrapBorder(value: Double, max: Double): Double = {
    val result =
      if (value >= max * 2.0)
        max
      else if (value >= max)
        value - max
      else if (value < max * -2.0)
        0
      else if (value < 0)
        max + value
      else
        value
    assert(result >= 0 && result < max)
    result
  }

  /** constrain a position to be within the playfield */
  private def wrapInPlayfield(pos: Vec2d): Vec2d = {
    Vec2d(
      wrapBorder(pos.x, playField.width),
      wrapBorder(pos.y, playField.height)
    )
  }

  /** move movable objects to their new location for this time period */
  private def moveObjects(deltaSeconds: Double): Unit = {
    mapSleds { sled =>
      val moved = sled.pos + (sled.speed * deltaSeconds)
      val wrapped = wrapInPlayfield(moved)
      sled.copy(pos = wrapped)
    }
  }

  private def checkCollisions(): Unit = {
    // TODO
  }

}
