package snowy.playfield

import scala.collection.mutable
import snowy.Awards.Travel
import snowy.GameConstants.{playfield, turnTime}
import vector.Vec2d
import Friction.friction
import Gravity.gravity
import Skid.skid

/** Moving objects in each game time slice */
object GameMotion {

  /** update sleds and snowballs speeds and positions */
  def moveSleds(sleds: Traversable[Sled], deltaSeconds: Double): Traversable[Travel] = {
    updateSledSpeedVector(sleds, deltaSeconds)
    repositionSleds(sleds, deltaSeconds)
  }

  /** move snowballs to their new location for this time period */
  def moveSnowballs(snowballs: TraversableOnce[Snowball], deltaSeconds: Double): Unit = {
    snowballs.foreach { snowball =>
      val wrappedPos = {
        val deltaPosition   = snowball.speed * deltaSeconds
        val newPosition     = snowball.pos + deltaPosition
        wrapInPlayfield(newPosition)
      }
      snowball.updatePos(wrappedPos)
    }
  }

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

  /** apply a push to a sled in the current direction of the turret */
  def pushSled(sled:Sled, deltaSeconds: Double): Unit = {
    val pushForceNow = (sled.pushForce / sled.mass) * deltaSeconds
    val pushVector = Vec2d.fromRotation(-sled.turretRotation) * pushForceNow
    val rawSpeed = sled.speed + pushVector
    sled.speed = rawSpeed.clipLength(sled.maxSpeed)
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
  private def repositionSleds(sleds: Traversable[Sled],
                              deltaSeconds: Double): Traversable[Travel] = {
    val awards = sleds.flatMap { sled =>
      val positionChange = sled.speed * deltaSeconds
      val moved          = sled.pos + positionChange
      val wrappedPos     = wrapInPlayfield(moved)
      sled.updatePos(wrappedPos)

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
