/** Moving objects in each game time slice */
trait GameMotion {
  self: GameControl =>

  val maxSpeed = 1000 // pixels / second

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
    val gravity = 250.0 // pixels / second / second
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
    import math._
    // friction is min when skis are aligned with direction, max when skis are at 90 degrees
    val friction = 250.0 // pixels / second / second
    val minFriction = 50.0 * deltaSeconds
    val frictionFactor = friction * deltaSeconds
    val brakeSteepness = .8  // higher means braking effect peaks narrowly when skis are near 90 to travel
    mapSleds { sled =>
      val speed = sled.speed.length
      speed match {
        case 0 => sled
        case _ =>
          val direction = sled.speed.unit
          val rotation = Vec2d.fromRotation(sled.rotation)
          val angleSkiToTravel = direction.angle(rotation)
          val rotationFactor = pow(abs(sin(angleSkiToTravel)), brakeSteepness)
          val sledFriction = minFriction + frictionFactor * rotationFactor // -speed in direction of travel

          val adjustedFriction = min(sledFriction, speed)
          val newSpeed = direction * (speed - adjustedFriction)
          println(s"direction: $direction  rotation: $rotation  angleSkiToTravel: $angleSkiToTravel  rotationFactor:$rotationFactor")
          println(s"speed: $speed  sledFriction: $sledFriction  newSpeed: $newSpeed")
          sled.copy(speed = newSpeed)
      }
    }
  }

  /** Adjust the speed based on the current rotation and speed */
  private def skidToRotate(deltaSeconds: Double): Unit = {
    val maxSkidSpeed = .8  // max seconds to complete a skid at full speed
    val rawSkidFactor = deltaSeconds / maxSkidSpeed

    /** return a revised sled with the speed adjusted for skidding effect */
    def skiddingSled(sled:SledState):SledState = {
      val speed = sled.speed.length
      speed match {
        case 0 => sled
        case _ =>
          val currentV = sled.speed / speed
          val targetV = Vec2d.fromRotation(sled.rotation)
          val skidFactor = rawSkidFactor * maxSpeed / speed
          val skid = math.min(1.0, skidFactor) // skidFactor of 1.0 means no more skid, rotate to targetV
          val skidVector = currentV + ((targetV - currentV) * skidFactor)
          val newSpeed = skidVector * speed
          sled.copy(speed = newSpeed)
      }
    }

    mapSleds (skiddingSled(_))
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
    if (result < 0 || result >= max) {
      println(s"wrapBorder error: $value < $max   $result")
    }
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
