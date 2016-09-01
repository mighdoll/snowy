case class Vec2d(x: Double, y: Double) {
  def +(other: Vec2d): Vec2d = Vec2d(x + other.x, y + other.y)

  def -(other: Vec2d): Vec2d = Vec2d(x - other.x, y - other.y)

  def *(d: Double): Vec2d = Vec2d(x * d, y * d)

  def /(d: Double): Vec2d = Vec2d(x / d, y / d)

  def length: Double = math.sqrt(x * x + y * y)

  def max(other: Vec2d): Vec2d = Vec2d(math.max(x, other.x), math.max(y, other.y))

  def max(m: Double): Vec2d = Vec2d(math.max(x, m), math.max(y, m))

  def min(other: Vec2d): Vec2d = Vec2d(math.min(x, other.x), math.min(y, other.y))

  def min(m: Double): Vec2d = Vec2d(math.min(x, m), math.min(y, m))

  def unit: Vec2d = this / length

  def zero: Boolean = (x == 0 && y == 0)

}
object Vec2d {
  /** @return a vector rotated angle radians from straight up
    * @param angle in radians
    */
  def rotation(angle:Double) : Vec2d = Vec2d(math.sin(angle), math.cos(angle))
}
