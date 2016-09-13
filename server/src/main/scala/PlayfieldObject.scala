/** A collidable object on the playfield */
trait PlayfieldObject {
  def size: Double

  def pos: Vec2d
}

/* rotation in radians, 0 points down the screen, towards larger Y values.
 * speed in pixels / second
 */
case class SledState(pos: Vec2d, size: Double, speed: Vec2d,
                     rotation: Double, turretRotation: Double) extends PlayfieldObject

case class TreeState(pos: Vec2d, size: Double) extends PlayfieldObject

case class SnowballState(pos: Vec2d, size: Double, speed: Vec2d) extends PlayfieldObject

case class User(name: String)