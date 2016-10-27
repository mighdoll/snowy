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

  /*
  TODO
  * launch angle of bullet (rear mounted cannons)
  * health max amount
  * health recovery rate
  * damage from sled-sled collision (spiky sled)
  * penetration factor for bullets (off axis hits bounce off modulo this factor)
  * armor?
  */
}

case object StationaryTestSled extends SledKind {
  override val gravity:Double = 0
}

case object BasicSled extends SledKind

case object TankSled extends SledKind {
  override val gravity:Double = -100
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
