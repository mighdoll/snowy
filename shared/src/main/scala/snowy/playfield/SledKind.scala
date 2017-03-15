package snowy.playfield

import snowy.GameConstants
import vector.Vec2d

sealed trait SledKind {

  /** acceleration in pixels / second / second */
  def gravity: Double = 0

  /** max speed of sled in pixels per second */
  def maxSpeed: Int = 400

  /** minimum time between shots, in milliseconds */
  def minRechargeTime: Int = 300

  /** factor increasing or decreasing damage from being hit with a snowball
    * beyond the mass and speed of the ball */
  def bulletImpactFactor: Double = GameConstants.Bullet.baseImpactFactor * bulletImpact

  /** factor increasing or decreasing damage from being hit with a snowball */
  protected def bulletImpact: Double = 1.0

  /** speed of bullet in pixels/sec */
  def bulletSpeed = 450

  /** radius in pixels */
  def bulletRadius = GameConstants.Bullet.averageRadius

  /** Bullet mass on collision */
  def bulletMass = .1

  /** acceleration due to recoil in pixels/sec/sec */
  // TODO: Bullet recoil should be a product of bulletMass
  def bulletRecoil = 0 //30

  /** bullet begins its flight this pixel offset from the sled center
    * if the sled is shooting straight up */
  def bulletLaunchPosition = Vec2d(0, 30)

  /** launch angle rotation from turret direction, e.g. for rear facing cannon
    * Angle is clockwise in screen coordinates (y is down) */
  def bulletLaunchAngle: Double = 0

  /** Initial health of a bullet. Bullets with enough health survive collisions and rebound */
  def bulletHealth: Double = .3

  /** Time before bullet expires in seconds */
  def bulletLifetime: Double = 3.5

  /** health of this sled. If it falls to zero, the sled dies. */
  def maxHealth: Double = 1

  /** time in seconds to recover 1 full point of health */
  def healthRecoveryTime: Double = 25.0

  /** deliver this amount of damage on collision with another sled at full speed */
  def maxImpactDamage: Double = .5

  /** reduce impact by this factor in sled/sled collisions */
  def armor: Double = 1.0

  /** sleds heavier than 1.0 accelerate and decelerate more slowly */
  def mass: Double = 1.0

  /** radius of the sled body */
  def radius: Double = 18

  /** speedup from drive mode. in pixels / second / second */
  def driveAcceleration: Double = 200

  /** speedup from the boost button. in pixels / second / second */
  def boostAcceleration: Double = 150

  /** minimum time between boosts, in seconds */
  def boostRecoveryTime: Double = 1

  /** maximum time for a boost, in seconds */
  def boostMaxDuration: Double = 1
  /*
  TODO
 * penetration factor for bullets (off axis hits bounce off modulo this factor)
 */
}

case object StationaryTestSled extends SledKind {
  override val gravity = 0.0
}

case object BasicSled extends SledKind

case object TankSled extends SledKind {
  override val gravity           = 0
  override val maxHealth         = 2.0
  override val minRechargeTime   = 1000
  override val bulletImpact      = 1.25
  override val bulletSpeed       = 250
  override val bulletRadius      = 10
  override val bulletMass        = .75
  override val bulletRecoil      = 0 //120
  override val bulletLifetime    = 10.0
  override val mass              = 3.0
  override val boostAcceleration = BasicSled.boostAcceleration * .5
  override val driveAcceleration = BasicSled.driveAcceleration * .5
}

case object GunnerSled extends SledKind {
  override val maxSpeed          = 350
  override val maxHealth         = .75
  override val minRechargeTime   = 100
  override val bulletSpeed       = 300
  override val bulletRadius      = 3
  override val bulletMass        = .01
  override val bulletImpact      = 5.5
  override val bulletRecoil      = 0 //10
  override val bulletHealth      = 1.0
  override val mass              = 1.0
  override val boostAcceleration = BasicSled.boostAcceleration * .8
  override val driveAcceleration = BasicSled.driveAcceleration * .8
}

case object SpeedySled extends SledKind {
  override val gravity            = 0
  override val healthRecoveryTime = 10.0
  override val mass               = .1
  override val minRechargeTime    = 200
  override val boostAcceleration  = BasicSled.boostAcceleration * 2.5
  override val driveAcceleration  = BasicSled.driveAcceleration * 2.5
  override val maxSpeed           = 500
}

case object SpikySled extends SledKind {
  override val gravity            = 0
  override val maxHealth          = 2.0
  override val maxImpactDamage    = 2.0
  override val mass               = 2.0
  override val healthRecoveryTime = 15.0
  override val boostAcceleration  = BasicSled.boostAcceleration * .65
  override val driveAcceleration  = BasicSled.driveAcceleration * .65
}

object SledKinds {
  val allSleds = Seq(BasicSled, TankSled, GunnerSled, SpeedySled, SpikySled)
}
