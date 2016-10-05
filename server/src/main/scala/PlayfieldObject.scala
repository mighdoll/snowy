/** A collidable object on the playfield */
trait PlayfieldObject {
  def size: Double

  def pos: Vec2d
}

case class Rect(pos: Vec2d, size: Vec2d)

case class Circle(pos: Vec2d, radius: Double)


/* rotation in radians, 0 points down the screen, towards larger Y values.
 * speed in pixels / second
 */
case class SledState(pos: Vec2d,
                     size: Double,
                     speed: Vec2d,
                     rotation: Double,
                     turretRotation: Double,
                     distanceTraveled: Double = 0,
                     health: Double = 1,
                     pushEnergy: Double = 1
                    ) extends PlayfieldObject

case class PushState(pushed: Double)

case class TreeState(pos: Vec2d, size: Double) extends PlayfieldObject

case class SnowballState(debugId:Int, pos: Vec2d, size: Double, speed: Vec2d, spawned: Long) extends PlayfieldObject

case class User(name: String, score: Double = 0)