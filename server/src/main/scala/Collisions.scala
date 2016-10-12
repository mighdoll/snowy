import vector.Vec2d

object Collisions {
  /** @return true if a circle and rect are overlapping */
  def circleRectCollide(circle: Circle, rect: Rect): Boolean = {
    // Find the nearest point to the circle within the rectangle
    val closestPoint = circle.pos.clamp(rect.pos, rect.pos + rect.size)

    val distanceSquared = (circle.pos - closestPoint).lengthSquared
    distanceSquared < Math.max(circle.radius * circle.radius, Double.MinPositiveValue)
  }

  def rectClosestPerimeterPoint(rect:Rect, circle:Circle): Vec2d = {
    // Find the nearest point to the circle within the rectangle, possibly in interior
    val closestPoint = circle.pos.clamp(rect.pos, rect.pos + rect.size)

    // TODO if point is interior to rectangle, move it to the edge
    closestPoint
  }
}