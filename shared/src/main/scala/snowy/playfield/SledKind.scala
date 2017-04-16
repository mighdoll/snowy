package snowy.playfield

import snowy.GameConstants
import vector.Vec2d

sealed trait SledKind {

  /** acceleration in pixels / second / second */
  def gravity: Double = -100

  /** max speed of sled in pixels per second */
  def maxSpeed: Int = 750

  /** minimum time between shots, in milliseconds */
  def minRechargeTime: Int = 200

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
  def bulletLifetime: Double = 4

  /** health of this sled. If it falls to zero, the sled dies. */
  def maxHealth: Double = 1

  /** time in seconds to recover 1 full point of health */
  def healthRecoveryTime: Double = 50.0

  /** deliver this amount of damage on collision with another sled at full speed */
  def maxImpactDamage: Double = .5

  /** reduce impact by this factor in sled/sled collisions */
  def armor: Double = 2.0

  /** sleds heavier than 1.0 accelerate and decelerate more slowly */
  def mass: Double = 1.0

  /** radius of the sled body */
  def radius: Double = 18

  /** Rotation to target speed in radians / second */
  def rotationSpeed: Double = math.Pi

  /** speedup from drive mode. in pixels / second / second */
  def driveAcceleration: Double = 150

  /** speedup from the boost button. in pixels / second / second */
  def boostAcceleration: Double = 400

  /** minimum time between boosts, in seconds */
  def boostRecoveryTime: Double = 1

  /** maximum time for a boost, in seconds */
  def boostMaxDuration: Double = 1

  /** friction from the slowdown button. in pixels / second / second */
  def brakeAcceleration: Double = 450
}

case object BasicSled extends SledKind

case object TankSled extends SledKind {
  override val gravity           = BasicSled.gravity * .35
  override val driveAcceleration = BasicSled.driveAcceleration * .25
  override val boostAcceleration = BasicSled.boostAcceleration * .5
  override val rotationSpeed     = math.Pi / 2
  override val maxImpactDamage   = 0.5
  override val maxHealth         = 3.0
  override val minRechargeTime   = 1000
  override val bulletHealth      = 2
  override val bulletImpact      = 0.25
  override val bulletSpeed       = 190
  override val bulletRadius      = 10
  override val bulletMass        = .75
  override val bulletRecoil      = 70
  override val bulletLifetime    = 10.0
  override val mass              = 3.0
}

case object GunnerSled extends SledKind {
  override val gravity            = BasicSled.gravity * 1.2
  override val driveAcceleration  = BasicSled.driveAcceleration * 1.2
  override val boostAcceleration  = BasicSled.boostAcceleration * .8
  override val maxSpeed           = 600
  override val maxHealth          = 1.5
  override val healthRecoveryTime = 10
  override val minRechargeTime    = 100
  override val bulletSpeed        = 400
  override val bulletRadius       = 3
  override val bulletLifetime     = 2
  override val bulletMass         = .01
  override val bulletImpact       = 4.5
  override val bulletHealth       = 0.3
  override val mass               = 1.0
}

case object SpeedySled extends SledKind {
  override val gravity            = BasicSled.gravity * 1.5
  override val driveAcceleration  = BasicSled.driveAcceleration * 2.5
  override val boostAcceleration  = BasicSled.boostAcceleration * 2.5
  override val brakeAcceleration  = BasicSled.brakeAcceleration * 2.5
  override val healthRecoveryTime = 10.0
  override val mass               = .1
  override val maxSpeed           = 700
}

case object SpikySled extends SledKind {
  override val gravity            = BasicSled.gravity * .5
  override val boostAcceleration  = BasicSled.boostAcceleration * .65
  override val driveAcceleration  = BasicSled.driveAcceleration * .5
  override val armor              = 1.0
  override val maxHealth          = 4.0
  override val maxImpactDamage    = 2.0
  override val mass               = 1.0
  override val rotationSpeed      = math.Pi * 2 / 3
  override val healthRecoveryTime = 10.0
  override val bulletHealth       = 2.0
  override val bulletSpeed        = 250
  override val bulletRadius       = 12
  override val bulletMass         = 30
  override val bulletImpact       = 0.002
  override val bulletLifetime     = 0.25
  override val minRechargeTime    = 800
}

case object PrototypeSled extends SledKind {
  override val gravity            = BasicSled.gravity * 1.5
  override val driveAcceleration  = BasicSled.driveAcceleration * 2.5
  override val boostAcceleration  = BasicSled.boostAcceleration * 2.5
  override val brakeAcceleration  = BasicSled.brakeAcceleration * 2.5
  override val healthRecoveryTime = 10.0
  override val mass               = .1
  override val maxSpeed           = 700
}
