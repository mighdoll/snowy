package snowy.collision

import snowy.playfield.{Circle, MovingCircle, Rect}
import vector.Vec2d

object Collisions {
  /** @return true if a circle and rect are overlapping */
  def circleRectCollide(circle: Circle, rect: Rect): Boolean = {
    // Find the nearest point to the circle within the rectangle
    val closestPoint = circle.pos.clamp(rect.pos, rect.pos + rect.size)

    val distanceSquared = (circle.pos - closestPoint).lengthSquared
    distanceSquared < Math.max(circle.radius * circle.radius, Double.MinPositiveValue)
  }

  def rectClosestPerimeterPoint(rect: Rect, circle: Circle): Vec2d = {
    // Find the nearest point to the circle within the rectangle, possibly in interior
    val closestPoint = circle.pos.clamp(rect.pos, rect.pos + rect.size)

    // TODO if point is interior to rectangle, move it to the edge
    closestPoint
  }

  /** Adjustments to speed and position of a collided object.
    *
    * (At first glance, it might seem simpler to apply the adjustments directly
    * to the collided object, but this separation allows adjustments from multiple
    * collisions to be accumulated.)
    */
  case class Collided[A <: MovingCircle](movingCircle: A, rebound: Vec2d, reposition: Vec2d)

  /** Collide two circular objects
    *
    * @return A list containing speed and position adjustments for two collided objects,
    *         or an empty list
    */
  def collideCircles[A <: MovingCircle](a: A, b: A): List[Collided[A]] = {
    val collisionDistance = a.radius + b.radius
    val collisionVector = (a.pos - b.pos) // vector between the centers
    val distance = collisionVector.length
    if (distance < collisionDistance) {

      // unit vectors in collision direction and perpendicular to collision
      val collisionUnit = collisionVector.unit
      val perpendicularUnit = collisionUnit.leftPerpendicular

      def bounce(speed: Vec2d, collision: Double): Vec2d = {
        val perpendicularComponent = perpendicularUnit * (speed dot perpendicularUnit)
        val collisionComponent = collisionUnit * collision
        val newSpeed = collisionComponent + perpendicularComponent
        newSpeed - speed
      }

      val bounceA = bounce(a.speed, b.speed dot collisionUnit)
      val bounceB = bounce(b.speed, a.speed dot collisionUnit)

      // move circles so they don't overlap
      val (repositionA, repositionB) = {
        val overlap = collisionDistance - distance
        val adjustPos = collisionUnit * (overlap / 2)
        (adjustPos, adjustPos * -1)
      }

      Collided(a, bounceA, repositionA) ::
        Collided(b, bounceB, repositionB) ::
        Nil
    } else {
      Nil
    }
  }

}