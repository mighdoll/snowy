package snowy.playfield

import java.util.concurrent.atomic.AtomicInteger
import vector.Vec2d

/** A collidable object on the playfield */
trait PlayfieldObject {
  type MyType <: PlayfieldObject
  def id: PlayId[MyType]

  def size: Double

  def pos: Vec2d
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
                pushEnergy: Double = 1
               ) extends PlayfieldObject {
  type MyType = Sled
}

case class Tree(id: TreeId = PlayfieldObject.nextId(),
                pos: Vec2d,
                size: Double
               ) extends PlayfieldObject {
  type MyType = Tree
}

case class Snowball(id: BallId = PlayfieldObject.nextId(),
                    ownerId: SledId,
                    pos: Vec2d,
                    size: Double,
                    speed: Vec2d,
                    spawned: Long
                   ) extends PlayfieldObject {
  type MyType = Snowball
}

case class User(name: String, score: Double = 0)