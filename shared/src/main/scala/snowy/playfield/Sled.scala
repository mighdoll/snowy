package snowy.playfield

import vector.Vec2d
import snowy.GameConstants.downhillRotation
import snowy.playfield.PlayfieldTracker.nullSledTracker

object Sled {
  val dummy = {
    val sled = new Sled(userName = "dummy")
    sled.position_=(Vec2d(-1,-1))(nullSledTracker)
    sled
  }

  def apply(userName: String): Sled = {
    new Sled(userName = userName)
  }

  def apply(userName: String,
            initialPosition: Vec2d,
            kind: SledKind,
            color: SkiColor)(implicit tracker:PlayfieldTracker[Sled]): Sled = {
    val sled = new Sled(
      userName = userName,
      kind = kind,
      skiColor = color,
      health = kind.maxHealth
    )
    sled.setInitialPosition(initialPosition)
  }
}

/* rotation in radians, 0 points down the screen, towards larger Y values.
 * speed in pixels / second
 */
case class Sled(userName: String,
                var speed: Vec2d = Vec2d.zero,
                kind: SledKind = BasicSled,
                skiColor: SkiColor = BasicSkis,
                /** angle of sled clockwise from screen coordinates of (0, 1) (down on screen)*/
                var rotation: Double = downhillRotation,
                var health: Double = 1,
                /** angle of turret clockwise in screen coordinates of (0, 1) down on screen */
                var turretRotation: Double = downhillRotation,
                var lastShotTime: Long = 0,
                var lastBoostTime: Long = 0)
    extends MovableCircularItem[Sled] {

  type MyType = Sled

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Sled]

  val driveMode = new SledDrive

  def radius: Double = kind.radius

  /** acceleration in pixels / second / second */
  def gravity: Double = kind.gravity

  /** max speed of sled in pixels per second */
  def maxSpeed: Double = kind.maxSpeed

  /** minimum time between shots, in milliseconds */
  def minRechargeTime: Int = kind.minRechargeTime

  /** factor increasing or decreasing damage from being hit with a snowball */
  def bulletImpactFactor: Double = kind.bulletImpactFactor

  /** speed of bullet in pixels/sec */
  def bulletSpeed: Int = kind.bulletSpeed

  /** radius in pixels */
  def bulletRadius: Int = kind.bulletRadius

  /** */
  def bulletMass: Double = kind.bulletMass

  /** acceleration due to recoil in pixels/sec/sec */
  def bulletRecoil: Int = kind.bulletRecoil

  /** bullet begins its flight this pixel offset from the sled center
    * if the sled is shooting straight up */
  def bulletLaunchPosition: Vec2d = kind.bulletLaunchPosition

  /** launch angle rotation from turret direction, e.g. for rear facing cannon */
  def bulletLaunchAngle: Double = kind.bulletLaunchAngle

  /** Initial health of a bullet. Bullets with enough health survive collisions and rebound */
  def bulletHealth: Double = kind.bulletHealth

  /** Time before a bullet expires in seconds */
  def bulletLifetime: Double = kind.bulletLifetime

  /** health of this sled. If it falls to zero, the sled dies. */
  def maxHealth: Double = kind.maxHealth

  /** time in seconds to recover 1 full point of health */
  def healthRecoveryTime: Double = kind.healthRecoveryTime

  /** health as a value between zero and one */
  def healthPercent: Double = health / kind.maxHealth

  /** deliver this amount of damage on collision with another sled at full speed */
  def maxImpactDamage: Double = kind.maxImpactDamage

  /** speedup from the boost button. in pixels / second / second */
  def boostAcceleration: Double = kind.boostAcceleration

  /** minimum time between boosts, in seconds */
  def boostRecoveryTime: Double = kind.boostRecoveryTime

  /** speedup from drive mode. in pixels / second / second */
  def driveAcceleration: Double = kind.driveAcceleration

  /** friction from the slowdown button. in pixels / second / second */
  def brakeAcceleration: Double = kind.brakeAcceleration

  /** reduce impact by this factor in sled/sled collisions */
  override def armor: Double = kind.armor

  /** sleds heavier than 1.0 accelerate and decelerate more slowly */
  override def mass: Double = kind.mass
}
