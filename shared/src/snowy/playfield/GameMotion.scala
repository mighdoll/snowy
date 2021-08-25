package snowy.playfield

import snowy.GameConstants.turnTime
import snowy.playfield.Friction.friction
import snowy.playfield.GameMotion._
import snowy.playfield.Skid.skid

object GameMotion {
  sealed trait Turning
  sealed trait Turn {
    def rotationSign: Int
  }
  case object NoTurn extends Turning
  case object LeftTurn extends Turning with Turn {
    override val rotationSign = 1
  }
  case object RightTurn extends Turning with Turn {
    override val rotationSign = -1
  }
}

/** Moving objects in each game time slice */
class GameMotion(playfield: Playfield) {

  /** update sleds and snowballs speeds and positions */
  def moveSleds(sleds: Iterable[Sled], deltaSeconds: Double, gameTime: Long)(
        implicit tracker: PlayfieldTracker[Sled]
  ): Unit = {

    driveSleds(sleds, deltaSeconds, gameTime)
    updateSledSpeedVector(sleds, deltaSeconds, gameTime)
    repositionSleds(sleds, deltaSeconds)
  }

  /** move snowballs to their new location for this time period */
  def moveSnowballs(snowballs: TraversableOnce[Snowball], deltaSeconds: Double)(
        implicit tracker: PlayfieldTracker[Snowball]
  ): Unit = {
    snowballs.foreach { snowball =>
      val wrappedPos = {
        val deltaPosition = snowball.speed * deltaSeconds
        val newPosition   = snowball.position + deltaPosition
        playfield.wrapInPlayfield(newPosition)
      }
      snowball.position = wrappedPos
    }
  }

  /** Rotate a sled at a rate controlled by GameConstants.turnTime
    *
    * @return a rotated sled instance
    */
  def turnSled(sled: Sled, direction: Turn, deltaSeconds: Double): Unit = {
    // TODO limit turn rate to e.g. 1 turn / 50msec to prevent cheating by custom clients?
    val turnDelta = direction.rotationSign * (math.Pi / turnTime) * deltaSeconds
    val max       = math.Pi * 2
    val min       = -math.Pi * 2
    val rotation  = sled.rotation + turnDelta
    val wrappedRotation =
      if (rotation > max) rotation - max
      else if (rotation < min) rotation - min
      else rotation
    sled.rotation = wrappedRotation
  }

  /** apply any pending but not yet cancelled commands from user drive,
    * e.g. braking or driving */
  private def driveSleds(sleds: Iterable[Sled],
                         deltaSeconds: Double,
                         gameTime: Long): Unit = {
    for (sled <- sleds) {
      sled.driveMode.driveSled(sled, deltaSeconds, gameTime)
    }
  }

  /** Update the direction and velocity of all sleds based on gravity and friction */
  private def updateSledSpeedVector(sleds: Iterable[Sled],
                                    deltaSeconds: Double,
                                    gameTime: Long): Unit = {
    sleds.foreach { sled =>
      import sled.{mass, rotation}
      val newSpeed = {
        val maxSpeed  = sled.currentMaxSpeed(gameTime)
        val afterSkid = skid(sled.speed, rotation, maxSpeed, mass, deltaSeconds)
        friction(afterSkid, rotation, deltaSeconds, mass)
      }

      sled.speed = newSpeed
    }
  }

  /** move the sleds to their new location for this time period */
  private def repositionSleds(sleds: Iterable[Sled], deltaSeconds: Double)(
        implicit tracker: PlayfieldTracker[Sled]
  ): Unit = {
    for {
      sled <- sleds
      positionChange = sled.speed * deltaSeconds
      moved          = sled.position + positionChange
      wrappedPos     = playfield.wrapInPlayfield(moved)
    } {
      sled.position = wrappedPos
    }
  }
}
