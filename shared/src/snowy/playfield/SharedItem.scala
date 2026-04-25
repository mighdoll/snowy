package snowy.playfield

import snowy.GameConstants.downhillRotation
import snowy.PlayIdTag.given
import snowy.playfield.PlayId.{BallId, PowerUpId, SledId}
import snowy.{playIdRW, GameConstants}
import upickle.default.{macroRW, ReadWriter}
import vector.Vec2d

/** A playfield item in a mutable set mirrored to clients */
sealed trait SharedItem

object SharedItem {
  implicit val rw: ReadWriter[SharedItem] = ReadWriter.merge(
    macroRW[HealthPowerUp],
    macroRW[SpeedPowerUp],
    macroRW[Snowball],
    macroRW[BasicSled],
    macroRW[SpeedySled],
    macroRW[TankSled]
  )
}

/** PowerUp hierarchy */
sealed trait PowerUp extends CircularItem[PowerUp] with SharedItem {
  def radius: Double = 5
  var internalPosition: Vec2d
}

object PowerUp {
  implicit val rw: ReadWriter[PowerUp] = ReadWriter.merge(
    macroRW[HealthPowerUp],
    macroRW[SpeedPowerUp]
  )
}

case class HealthPowerUp(override val id: PowerUpId, var internalPosition: Vec2d)
    extends PowerUp {
  override def canEqual(a: Any): Boolean = a.isInstanceOf[HealthPowerUp]
}

case class SpeedPowerUp(override val id: PowerUpId, var internalPosition: Vec2d)
    extends PowerUp {
  override def canEqual(a: Any): Boolean = a.isInstanceOf[SpeedPowerUp]
}

/** Snowball */
case class Snowball(
      override val id: BallId,
      ownerId: SledId,
      var speed: Vec2d,
      radius: Double,
      mass: Double,
      spawned: Long,
      var health: Double,
      lifetime: Double,
      override val impactDamage: Double,
      var internalPosition: Vec2d
) extends MovableCircularItem[Snowball] with SharedItem {
  override def canEqual(a: Any): Boolean = a.isInstanceOf[Snowball]
}

object Snowball {
  implicit val rw: ReadWriter[Snowball] = macroRW

  def apply(
        ownerId: SledId,
        speed: Vec2d,
        radius: Double,
        mass: Double,
        spawned: Long,
        health: Double,
        lifetime: Double,
        impactDamage: Double
  )(implicit tracker: PlayfieldTracker[Snowball]): Snowball = {
    val snowball = new Snowball(
      PlayId.nextId[Snowball](),
      ownerId,
      speed,
      radius,
      mass,
      spawned,
      health,
      lifetime,
      impactDamage,
      Vec2d.zero
    )
    snowball
  }
}

/** Sled hierarchy */
sealed trait Sled extends MovableCircularItem[Sled] with SharedItem {
  val userName: String
  val skiColor: SkiColor
  var internalPosition: Vec2d

  var speed: Vec2d
  var rotation: Double
  var health: Double
  var lastShotTime: Long
  var lastBoostTime: Long

  var maxSpeed: Int
  val maxSpeedBoost: DecayingBoost
  val driveMode: SledDrive
  var maxHealth: Double
  var driveAcceleration: Double

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
  def bulletRecoil: Int = 0 // 30

  /** bullet begins its flight this pixel offset from the sled radius if the sled is
    * shooting straight up
    */
  def bulletLaunchPosition = Vec2d(0, 10)

  /** Initial health of a bullet. Bullets with enough health survive collisions and
    * rebound
    */
  def bulletHealth: Double = .3

  /** Time before bullet expires in seconds */
  def bulletLifetime: Double = 2

  /** time in seconds to recover 1 full point of health */
  def healthRecoveryTime: Double = 50.0

  /** deliver this amount of damage on collision with another sled at full speed */
  override def impactDamage: Double = .01

  /** sleds heavier than 1.0 accelerate and decelerate more slowly */
  def mass: Double = 1.0

  /** radius of the sled body */
  def radius: Double = Sled.basicRadius

  /** minimum time between boosts, in seconds */
  def boostRecoveryTime: Double = 1

  /** maximum time for a boost, in seconds */
  def boostDuration: Double = 0.6

  /** Boost acceleration in pixels / second / second */
  def boostAcceleration: Int = 6000

  /** friction from the slowdown button. in pixels / second / second */
  def brakeAcceleration: Double = 450
}

object Sled {
  implicit val rw: ReadWriter[Sled] = ReadWriter.merge(
    macroRW[BasicSled],
    macroRW[SpeedySled],
    macroRW[TankSled]
  )

  val dummy = {
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullSledTracker
    Sled("dummy", Vec2d(-1, -1))(using nullSledTracker)
  }

  def apply(
        userName: String,
        initialPosition: Vec2d = Vec2d.zero,
        sledType: SledType = BasicSledType,
        color: SkiColor = BasicSkis
  )(implicit tracker: PlayfieldTracker[Sled]): Sled = {
    val sled =
      sledType match {
        case BasicSledType =>
          new BasicSled(PlayId.nextId[Sled](), userName, color, initialPosition)
        case SpeedySledType =>
          new SpeedySled(PlayId.nextId[Sled](), userName, color, initialPosition)
        case TankSledType =>
          new TankSled(PlayId.nextId[Sled](), userName, color, initialPosition)
      }
    sled.setInitialPosition(initialPosition)
    sled
  }

  def basicRadius = 18.0
}

sealed trait SledType derives ReadWriter
case object BasicSledType  extends SledType
case object SpeedySledType extends SledType
case object TankSledType   extends SledType

case class BasicSled(
      override val id: SledId,
      override val userName: String,
      override val skiColor: SkiColor = BasicSkis,
      var internalPosition: Vec2d,
      var speed: Vec2d = Vec2d.zero,
      var rotation: Double = downhillRotation,
      var health: Double = 1,
      var lastShotTime: Long = 0,
      var lastBoostTime: Long = 0,
      var maxSpeed: Int = 300,
      override val maxSpeedBoost: DecayingBoost = DecayingBoost(),
      override val driveMode: SledDrive = SledDrive(),
      var maxHealth: Double = 1.0,
      var driveAcceleration: Double = 1000.0
) extends Sled {
  override def canEqual(a: Any): Boolean = a.isInstanceOf[BasicSled]
}

case class SpeedySled(
      override val id: SledId,
      override val userName: String,
      override val skiColor: SkiColor = BasicSkis,
      var internalPosition: Vec2d,
      var speed: Vec2d = Vec2d.zero,
      var rotation: Double = downhillRotation,
      var health: Double = 1,
      var lastShotTime: Long = 0,
      var lastBoostTime: Long = 0,
      var maxSpeed: Int = 300,
      override val maxSpeedBoost: DecayingBoost = DecayingBoost(),
      override val driveMode: SledDrive = SledDrive(),
      var maxHealth: Double = 1.0,
      var driveAcceleration: Double = 1500.0
) extends Sled {
  override def canEqual(a: Any): Boolean = a.isInstanceOf[SpeedySled]

  override val brakeAcceleration  = 112.5
  override val healthRecoveryTime = 10.0
  override val mass               = .1
  override val minRechargeTime    = 450
  override val bulletImpact       = 1.2
}

case class TankSled(
      override val id: SledId,
      override val userName: String,
      override val skiColor: SkiColor = BasicSkis,
      var internalPosition: Vec2d,
      var speed: Vec2d = Vec2d.zero,
      var rotation: Double = downhillRotation,
      var health: Double = 1,
      var lastShotTime: Long = 0,
      var lastBoostTime: Long = 0,
      var maxSpeed: Int = 300,
      override val maxSpeedBoost: DecayingBoost = DecayingBoost(),
      override val driveMode: SledDrive = SledDrive(),
      var maxHealth: Double = 3.0,
      var driveAcceleration: Double = 250.0
) extends Sled {
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
}
