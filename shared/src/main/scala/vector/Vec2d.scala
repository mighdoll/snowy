package vector

case class Vec2d(x: Double, y: Double) {
  def +(other: Vec2d): Vec2d = Vec2d(x + other.x, y + other.y)

  def -(other: Vec2d): Vec2d = Vec2d(x - other.x, y - other.y)

  def *(d: Double): Vec2d = Vec2d(x * d, y * d)

  def /(d: Double): Vec2d = Vec2d(x / d, y / d)

  def length: Double = math.sqrt(x * x + y * y)

  def lengthSquared: Double = x * x + y * y

  def max(other: Vec2d): Vec2d = Vec2d(math.max(x, other.x), math.max(y, other.y))

  def max(m: Double): Vec2d = Vec2d(math.max(x, m), math.max(y, m))

  def min(other: Vec2d): Vec2d = Vec2d(math.min(x, other.x), math.min(y, other.y))

  def min(m: Double): Vec2d = Vec2d(math.min(x, m), math.min(y, m))

  def abs: Vec2d = Vec2d(math.abs(x), math.abs(y))

  def leftPerpendicular: Vec2d = Vec2d(y, -x)

  def rightPerpendicular: Vec2d = Vec2d(-y, x)

  def unit: Vec2d = {
    val l = length
    if (l == 0) {
      println(s"returning unitUp from zero length Vec2.unit")
      Vec2d.unitUp
    } else {
      this / l
    }
  }

  def zero: Boolean = (x == 0 && y == 0)

  def angle(other: Vec2d): Double =
    math.atan2(this cross other, this dot other)

  def rotate(radians: Double): Vec2d = {
    val sin = math.sin(radians)
    val cos = math.cos(radians)
    Vec2d(cos * x - sin * y, sin * x + cos * y)
  }

  def dot(other: Vec2d): Double = (x * other.x) + (y * other.y)

  def cross(other: Vec2d): Double = (x * other.y) - (y * other.x)

  /** Make sure vector.Vec2d is within max & min */
  def clamp(min: Vec2d, max: Vec2d): Vec2d = {
    def clampNum(num: Double, min: Double, max: Double): Double = {
      Math.min(Math.max(num, min), max)
    }
    Vec2d(clampNum(x, min.x, max.x), clampNum(y, min.y, max.y))
  }

  /** Apply a partial function to transform this vector.
    *
    * @return the result of the partial function, or this vector if the function is not defined
    */
  def transform(pFn: PartialFunction[Vec2d, Vec2d]): Vec2d = {
    if (pFn.isDefinedAt(this)) pFn(this)
    else this
  }
}

object Vec2d {

  /** @return a vector rotated from straight up vector.Vec2d(0,1)
    * @param angle in radians
    */
  def fromRotation(angle: Double): Vec2d = Vec2d(math.sin(angle), math.cos(angle))

  val unitUp = Vec2d(0, 1)

  val unitRight = Vec2d(1, 0)

  val unitDown = Vec2d(0, -1)

  val unitLeft = Vec2d(-1, 0)

  val zero = Vec2d(0, 0)
}
