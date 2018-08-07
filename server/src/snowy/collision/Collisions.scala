package snowy.collision

import snowy.playfield._
import vector.Vec2d

object Collisions {

  // TODO move to snowy.playfield.Intersect
  /** @return true if a circle and rect are overlapping */
  def circleRectCollide(circle: Circle, rect: Rect): Boolean = {
    // Find the nearest point to the circle within the rectangle
    val closestPoint = circle.pos.clamp(rect.pos, rect.pos + rect.size)

    val distanceSquared = (circle.pos - closestPoint).lengthSquared
    distanceSquared < math.max(circle.radius * circle.radius, Double.MinPositiveValue)
  }

  def rectClosestPerimeterPoint(rect: Rect, circle: Circle): Vec2d = {
    // Find the nearest point to the circle within the rectangle, possibly in interior
    val closestPoint = circle.pos.clamp(rect.pos, rect.pos + rect.size)

    // LATER if point is interior to rectangle, move it to the edge
    closestPoint
  }

  def circularCollide[A <: CircularItem[A], B <: CircularItem[B]](a: A, b: B): Boolean = {

    val collisionDistance = a.radius + b.radius
    val collisionVector   = a.position - b.position // vector between the centers
    val distance          = collisionVector.length
    distance < collisionDistance
  }

  /** Collide two circular objects
    *
    * @return A list containing speed and position adjustments for two collided objects,
    *         or an empty list
    */
  def collideCircles[A <: MovableCircularItem[A], B <: MovableCircularItem[B]](
        a: A,
        b: B
  ): Option[(Collided[A], Collided[B])] = {
    val collisionDistance = a.radius + b.radius
    val collisionVector   = a.position - b.position // vector between the centers
    val distance          = collisionVector.length
    if (distance < collisionDistance) {

      // unit vectors in collision direction and perpendicular to collision
      val collisionUnit     = collisionVector.unit
      val perpendicularUnit = collisionUnit.leftPerpendicular

      def bounce(speed1: Vec2d, speed2: Vec2d, m1: Double, m2: Double): Vec2d = {
        val collisionComponent = {
          val v1    = speed1 dot collisionUnit
          val v2    = speed2 dot collisionUnit
          val newV1 = (v1 * (m1 - m2) + (2 * m2 * v2)) / (m1 + m2)
          collisionUnit * newV1
        }
        val perpendicularComponent = perpendicularUnit * (speed1 dot perpendicularUnit)
        val newSpeed               = collisionComponent + perpendicularComponent
        newSpeed - speed1
      }

      val bounceA = bounce(a.speed, b.speed, a.mass, b.mass)
      val bounceB = bounce(b.speed, a.speed, b.mass, a.mass)

      // move circles so they don't overlap
      val (repositionA, repositionB) = {
        val overlap   = collisionDistance - distance
        val adjustPos = collisionUnit * (overlap / 2)
        (adjustPos, adjustPos * -1)
      }

      Some(Collided(a, bounceA, repositionA), Collided(b, bounceB, repositionB))
    } else {
      None
    }
  }

  /** Adjustments to speed and position of a collided object.
    *
    * (At first glance, it might seem simpler to apply the adjustments directly
    * to the collided object, but this separation allows adjustments from multiple
    * collisions to be accumulated.)
    */
  case class Collided[A <: MovableCircularItem[A]](item: A,
                                                   rebound: Vec2d,
                                                   reposition: Vec2d)

}
