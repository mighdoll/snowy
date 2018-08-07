package snowy.playfield

import snowy.GameConstants
import snowy.GameConstants.downhillRotation
import vector.Vec2d

object Sled {
  val dummy = {
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullSledTracker
    Sled("dummy", Vec2d(-1, -1))(nullSledTracker)
  }

  def apply(userName: String,
            initialPosition: Vec2d = Vec2d.zero,
            sledType: SledType = BasicSledType,
            color: SkiColor = BasicSkis)(
        implicit tracker: PlayfieldTracker[Sled]
  ): Sled = {
    val sled =
      sledType match {
        case BasicSledType  => new BasicSled(userName, color)
        case SpeedySledType => new SpeedySled(userName, color)
        case TankSledType   => new TankSled(userName, color)
      }
    sled.setInitialPosition(initialPosition)
  }

  def basicRadius = 18.0
}

sealed trait Sled extends MovableCircularItem[Sled] with SharedItem {
  val userName: String
  val skiColor: SkiColor = BasicSkis
  var speed: Vec2d       = Vec2d.zero

  /** angle of sled clockwise from screen coordinates of (0, 1) (down on screen)*/
  var rotation: Double = downhillRotation
  var health: Double   = 1

  var lastShotTime: Long  = 0
  var lastBoostTime: Long = 0

  val driveMode = new SledDrive()

  /** max speed of sled in pixels per second */
  var maxSpeed: Int = 300
  val maxSpeedBoost = new DecayingBoost()
  def currentMaxSpeed(gameTime: Long): Int = {
    maxSpeed + maxSpeedBoost.current(gameTime)
  }

  /** minimum time between shots, in milliseconds */
  def minRechargeTime: Int = 700

  /** factor increasing or decreasing damage from being hit with a snowball */
  def bulletImpact: Double = 5.0

  /** speed of bullet in pixels/sec */
  def bulletSpeed: Int = 500

  /** radius in pixels */
  def bulletRadius: Int = GameConstants.Bullet.averageRadius

  /** Bullet mass on collision */
  def bulletMass: Double = .1

  /** acceleration due to recoil in pixels/sec/sec */
  // TODO: Bullet recoil should be a product of bulletMass
  def bulletRecoil: Int = 0 //30

  /** bullet begins its flight this pixel offset from the sled radius
    * if the sled is shooting straight up */
  def bulletLaunchPosition = Vec2d(0, 10)

  /** Initial health of a bullet. Bullets with enough health survive collisions and rebound */
  def bulletHealth: Double = .3

  /** Time before bullet expires in seconds */
  def bulletLifetime: Double = 2

  /** health of this sled. If it falls to zero, the sled dies. */
  var maxHealth: Double = 1.0

  /** time in seconds to recover 1 full point of health */
  def healthRecoveryTime: Double = 50.0

  /** deliver this amount of damage on collision with another sled at full speed */
  override def impactDamage: Double = .01

  /** sleds heavier than 1.0 accelerate and decelerate more slowly */
  def mass: Double = 1.0

  /** radius of the sled body */
  def radius: Double = Sled.basicRadius

  /** speedup from drive mode. in pixels / second / second */
  var driveAcceleration: Double = 1000

  /** minimum time between boosts, in seconds */
  def boostRecoveryTime: Double = 1

  /** maximum time for a boost, in seconds */
  def boostDuration: Double = 0.6

  /** Boost acceleration in pixels / second / second */
  def boostAcceleration: Int = 6000

  /** friction from the slowdown button. in pixels / second / second */
  def brakeAcceleration: Double = 450
}

sealed trait SledType
case object BasicSledType  extends SledType
case object SpeedySledType extends SledType
case object TankSledType   extends SledType

class BasicSled(override val userName: String,
                override val skiColor: SkiColor = BasicSkis)
    extends Sled {
  override def canEqual(a: Any): Boolean = a.isInstanceOf[BasicSled]
}

class SpeedySled(override val userName: String,
                 override val skiColor: SkiColor = BasicSkis)
    extends Sled {
  override def canEqual(a: Any): Boolean = a.isInstanceOf[SpeedySled]

  override val brakeAcceleration  = 112.5
  override val healthRecoveryTime = 10.0
  override val mass               = .1
  override val minRechargeTime    = 450
  override val bulletImpact       = 1.2

  driveAcceleration *= 1.5
}

class TankSled(override val userName: String, override val skiColor: SkiColor = BasicSkis)
    extends Sled {
  override def canEqual(a: Any): Boolean = a.isInstanceOf[TankSled]

  override val impactDamage    = 0.5
  override val minRechargeTime = 1000
  override val bulletHealth    = 0.5
  override val bulletImpact    = 0.1
  override val bulletSpeed     = 180
  override val bulletRadius    = 10
  override val bulletMass      = .75
  override val bulletRecoil    = 0
  override val bulletLifetime  = 10.0
  override val mass            = 3.0

  maxHealth = 3.0
  driveAcceleration *= 0.25
}
