package snowy

import vector.Vec2d

sealed trait SledKind {
  /** acceleration in pixels / second / second */
  def gravity: Double = -250

  /** max speed of sled in pixels per second */
  def maxSpeed: Int = 700

  /** minimum time between shots, in milliseconds */
  def minRechargeTime: Int = 300

  /** health cost from being hit with a snowball */
  def bulletPower: Double = .4

  /** speed of bullet in pixels/sec */
  def bulletSpeed = 400

  /** radius in pixels */
  def bulletSize = 12

  /** acceleration due to recoil in pixels/sec/sec */
  def bulletRecoil = 30

  /** bullet begins its flight this pixel offset from the sled center
    * if the sled is shooting straight up */
  def bulletLaunchPosition = Vec2d(0, 30)

  /** launch angle rotation from turret direction, e.g. for rear facing cannon */
  def bulletLaunchAngle: Double = 0

  /** health of this sled. If it falls to zero, the sled dies. */
  def maxHealth: Double = 1

  /** time in seconds to recover 1 full point of health */
  def healthRecoveryTime: Double = 25.0

  /** deliver this amount of damage on collision with another sled at full speed */
  def maxImpactDamage: Double = .5

  /** reduce impact by this factor in sled/sled collisions */
  def armor: Double = 1.0

  /*
  TODO
  * penetration factor for bullets (off axis hits bounce off modulo this factor)
  */
}

case object StationaryTestSled extends SledKind {
  override val gravity: Double = 0
}

case object BasicSled extends SledKind

case object TankSled extends SledKind {
  override val gravity: Double = -100
  override val maxSpeed = 200
  override val minRechargeTime = 500
  override val bulletPower = .2
  override val bulletSpeed = 300
  override val bulletSize = 9
}

case object GunnerSled extends SledKind {
  override val maxSpeed = 500
  override val minRechargeTime = 1000
  override val bulletPower = .8
  override val bulletSpeed = 500
  override val bulletSize = 20
  override val bulletRecoil = 120
}

case object SpikySled extends SledKind {
  override val maxImpactDamage = 1.0
}

