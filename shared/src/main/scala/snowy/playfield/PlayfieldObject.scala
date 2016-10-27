package snowy.playfield

import java.util.concurrent.atomic.AtomicInteger
import snowy.{BasicSled, SledKind}
import vector.Vec2d

/** A collidable object on the playfield */
trait PlayfieldObject {
  type MyType <: PlayfieldObject

  def id: PlayId[MyType]

  def size: Double

  def pos: Vec2d

  def updatePos(newPos: Vec2d): MyType
}

object PlayfieldObject {
  val counter = new AtomicInteger()

  def nextId[A <: PlayfieldObject](): PlayId[A] = PlayId(counter.getAndIncrement())
}

case class PlayId[A](val id: Int) extends AnyVal

object PlayId {
  type SledId = PlayId[Sled]
  type BallId = PlayId[Snowball]
  type TreeId = PlayId[Tree]
}

import PlayId._

case class Rect(pos: Vec2d, size: Vec2d)

case class Circle(pos: Vec2d, radius: Double)

trait MovingCircle {
  type MyType <: MovingCircle

  def pos: Vec2d

  def speed: Vec2d

  def radius: Double

  def updateSpeed(newSpeed: Vec2d): MyType
}

object Sled {
  val dummy = Sled(PlayId(-1), "dummy", Vec2d.zero, 0, Vec2d.zero, 0, 0)
}

/* rotation in radians, 0 points down the screen, towards larger Y values.
 * speed in pixels / second
 */
case class Sled(id: SledId = PlayfieldObject.nextId(),
                userName: String,
                pos: Vec2d,
                size: Double,
                speed: Vec2d,
                rotation: Double,
                turretRotation: Double,
                health: Double = 1,
                pushEnergy: Double = 1,
                lastShotTime: Long = 0,
                kind: SledKind = BasicSled
               ) extends PlayfieldObject with MovingCircle {
  override def updatePos(newPos: Vec2d): Sled = {
    this.copy(pos = newPos)
  }

  override def radius = size / 2

  override def updateSpeed(newSpeed: Vec2d): Sled = this.copy(speed = newSpeed)

  /** acceleration in pixels / second / second */
  def gravity: Double = kind.gravity

  /** max speed of sled in pixels per second */
  def maxSpeed: Double = kind.maxSpeed

  /** minimum time between shots, in milliseconds */
  def minRechargeTime: Int = kind.minRechargeTime

  /** health cost from being hit with a snowball */
  def bulletPower: Double = kind.bulletPower

  /** speed of bullet in pixels/sec */
  def bulletSpeed: Int = kind.bulletSpeed

  /** radius in pixels */
  def bulletSize: Int = kind.bulletSize

  /** acceleration due to recoil in pixels/sec/sec */
  def bulletRecoil: Int = kind.bulletRecoil

  /** bullet begins its flight this pixel offset from the sled center
    * if the sled is shooting straight up */
  def bulletLaunchPosition: Vec2d = kind.bulletLaunchPosition

  /** launch angle rotation from turret direction, e.g. for rear facing cannon */
  def bulletLaunchAngle: Double = kind.bulletLaunchAngle

  type MyType = Sled
}

case class Tree(id: TreeId = PlayfieldObject.nextId(),
                pos: Vec2d,
                size: Double
               ) extends PlayfieldObject {
  type MyType = Tree

  override def updatePos(newPos: Vec2d): Tree = {
    this.copy(pos = newPos)
  }
}

case class Snowball(id: BallId = PlayfieldObject.nextId(),
                    ownerId: SledId,
                    pos: Vec2d,
                    size: Double,
                    speed: Vec2d,
                    spawned: Long,
                    power: Double
                   ) extends PlayfieldObject {
  type MyType = Snowball

  override def updatePos(newPos: Vec2d): Snowball = {
    this.copy(pos = newPos)
  }
}

case class User(name: String, score: Double = 0, robot: Boolean = false)

