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

case class Rect(pos: Vec2d, size: Vec2d)

case class Circle(pos: Vec2d, radius: Double)


/* rotation in radians, 0 points down the screen, towards larger Y values.
 * speed in pixels / second
 */
case class Sled(id: PlayId[Sled],
                userName: String,
                pos: Vec2d,
                size: Double,
                speed: Vec2d,
                rotation: Double,
                turretRotation: Double,
                distanceTraveled: Double = 0,
                health: Double = 1,
                pushEnergy: Double = 1
               ) extends PlayfieldObject {
  type MyType = Sled
}

case class Tree(id: PlayId[Tree],
                pos: Vec2d,
                size: Double
               ) extends PlayfieldObject {
  type MyType = Tree
}

case class Snowball(id: PlayId[Snowball],
                    ownerId: PlayId[Sled],
                    pos: Vec2d,
                    size: Double,
                    speed: Vec2d,
                    spawned: Long
                   ) extends PlayfieldObject {
  type MyType = Snowball
}

case class User(name: String, score: Double = 0)