package snowy.playfield

import scala.collection.mutable
import snowy.Awards.Travel
import snowy.GameConstants.turnTime
import vector.Vec2d
import Friction.friction
import Gravity.gravity
import Skid.skid
import snowy.playfield.GameMotion._

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
class GameMotion(playfield:Playfield) {

  /** update sleds and snowballs speeds and positions */
  def moveSleds(sleds: Traversable[Sled], deltaSeconds: Double)(
        implicit tracker: PlayfieldTracker[Sled]
  ): Traversable[Travel] = {

    updateSledSpeedVector(sleds, deltaSeconds)
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
    val rotation  = sled.turretRotation + turnDelta
    val wrappedRotation =
      if (rotation > max) rotation - max
      else if (rotation < min) rotation - min
      else rotation
    sled.turretRotation = wrappedRotation
  }


  /** Update the direction and velocity of all sleds based on gravity and friction */
  private def updateSledSpeedVector(sleds: Traversable[Sled],
                                    deltaSeconds: Double): Unit = {
    sleds.foreach { sled =>
      import sled.{gravity => grav, mass, maxSpeed, rotation}
      val newSpeed = {
        val afterGravity = gravity(sled.speed, rotation, maxSpeed, grav, deltaSeconds)
        val afterSkid    = skid(afterGravity, rotation, maxSpeed, mass, deltaSeconds)
        friction(afterSkid, rotation, deltaSeconds, mass)
      }

      sled.speed = newSpeed
    }
  }

  /** move the sleds to their new location for this time period */
  private def repositionSleds(sleds: Traversable[Sled], deltaSeconds: Double)(
        implicit tracker: PlayfieldTracker[Sled]
  ): Traversable[Travel] = {
    val awards = sleds.flatMap { sled =>
      val positionChange = sled.speed * deltaSeconds
      val moved          = sled.position + positionChange
      val wrappedPos     = playfield.wrapInPlayfield(moved)
      sled.position = wrappedPos

      val distance = positionChange.length
      if (distance > 0) {
        Some(Travel(sled.id, distance))
      } else {
        None
      }
    }

    awards
  }
}
