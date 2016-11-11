package snowy.playfield

import java.util.concurrent.atomic.AtomicInteger

import vector.Vec2d

/** A collidable object on the playfield */
trait PlayfieldObject {
  type MyType <: PlayfieldObject

  def id: PlayId[MyType]

  protected var _position: Vec2d

  var health: Double

  def armor: Double

  def impactDamage: Double

  def pos: Vec2d = _position

  def updatePos(newPos: Vec2d): Unit = _position = newPos // TODO update grid

  def copyWithUpdatedPos(newPos: Vec2d): MyType

  def canEqual(a: Any): Boolean = a.isInstanceOf[PlayfieldObject]

  override def hashCode(): Int = id.id

  override def equals(that: Any): Boolean = that match {
    case that: PlayfieldObject => canEqual(that) && id == that.id
    case _ => false
  }
}

trait MoveablePlayfieldObject extends PlayfieldObject {
  var speed: Vec2d
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

case class Rect(pos: Vec2d, size: Vec2d)

case class Circle(pos: Vec2d, radius: Double)
