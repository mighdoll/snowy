import GameConstants._

/** Moving objects in each game time slice */
trait GameMotion {
  self: GameState =>

  /** update sleds and snowballs speeds and positions */
  protected def moveStuff(deltaSeconds: Double): Unit = {
    updateSledSpeedVector(deltaSeconds)
    moveSleds(deltaSeconds)
    moveSnowballs(deltaSeconds)
    checkCollisions()
  }

  /** Update the direction and velocity of all sleds based on gravity and friction */
  def updateSledSpeedVector(deltaSeconds: Double): Unit = {
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

  /** constrain a position to be within the playfield */
  def wrapInPlayfield(pos: Vec2d): Vec2d = {
    Vec2d(
      wrapBorder(pos.x, playfield.x),
      wrapBorder(pos.y, playfield.y)
    )
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

  /** move snowballs to their new location for this time period */
  private def moveSnowballs(deltaSeconds: Double): Unit = {
    snowballs.replaceItems{snowball =>
      snowball.copy(pos = wrapInPlayfield(snowball.pos + snowball.speed))
    }
  }

  /** move the sleds to their new location for this time period */
  private def moveSleds(deltaSeconds: Double): Unit = {
    sleds.replaceItems { sled =>
      val positionChange = sled.speed * deltaSeconds
      val moved = sled.pos + positionChange
      val wrapped = wrapInPlayfield(moved)
      val distance = positionChange.length
      sled.copy(pos = wrapped, distanceTraveled = distance)
    }
  }

  /** check for collisions between the sled and trees or snowballs */
  private def checkCollisions(): Unit = {
    import GameCollide.snowballTrees
    val collisions = sleds.map{(id, sled) =>
      val collide = new SledCollide(id, sled, snowballs, trees)
      val optSled = collide.tree().orElse(collide.snowball())
      optSled.map{newSled => (id, newSled)}
    }

    collisions.flatten.foreach { case (id, newSled) =>
      sleds.remove(id)
      sleds.add(id, newSled)
    }

    snowballs.removeMatchingItems(snowballTrees(_, trees))
  }

}

