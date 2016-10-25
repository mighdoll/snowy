package snowy

object SledKinds {
  val sledKinds = Seq(StationaryTestSled, BasicSled, TankSled)
}

sealed trait SledKind {
  /** acceleration in pixels / second / second */
  def gravity: Double = -250

  /** max speed of sled in pixels per second */
  def maxSpeed: Int = 1000

  /** minimum time between shots, in milliseconds */
  def minRechargeTime: Int = 300

  /** health cost from being hit with a snowball */
  def bulletPower: Double = .4

  /** speed of bullet in pixels/sec */
  def bulletSpeed = 300

  /** radius in pixels */
  def bulletSize = 12

  /** acceleration due to recoil in pixels/sec/sec */
  def bulletRecoil = 30
}

case object StationaryTestSled extends SledKind {
  override val gravity:Double = 0
}

case object BasicSled extends SledKind

case object TankSled extends SledKind {
  override val gravity:Double = -100
  override val maxSpeed = 200
  override val minRechargeTime = 750
  override val bulletPower = .8
  override val bulletSpeed = 500
  override val bulletSize = 20
  override val bulletRecoil = 120
}
