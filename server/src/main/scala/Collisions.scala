object Collisions {

  case class Rect(pos: Vec2d, size: Vec2d)

  case class Circle(pos: Vec2d, radius: Double)

  /** @return true if a circle and rect are overlapping */
  def circleRectCollide(circle: Circle, rect: Rect): Boolean = {
    // Find the nearest point to the circle within the rectangle
    val closestPoint = circle.pos.clamp(rect.pos, rect.pos + rect.size)

    val distanceSquared = (circle.pos - closestPoint).lengthSquared
    distanceSquared < Math.max(circle.radius * circle.radius, Double.MinPositiveValue)
  }
}