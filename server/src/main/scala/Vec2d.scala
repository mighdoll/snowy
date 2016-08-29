case class Vec2d(x:Double, y:Double) {
  def +(other:Vec2d):Vec2d = Vec2d(x+other.x, y+other.y)
  def -(other:Vec2d):Vec2d = Vec2d(x-other.x, y-other.y)
  def *(d:Double):Vec2d = Vec2d(x*d, y*d)
  def max(other:Vec2d):Vec2d = Vec2d(Math.max(x,other.x), Math.max(y,other.y))
  def max(m:Double):Vec2d = Vec2d(Math.max(x,m), Math.max(y,m))
  def min(other:Vec2d):Vec2d = Vec2d(Math.min(x,other.x), Math.min(y,other.y))
  def min(m:Double):Vec2d = Vec2d(Math.min(x,m), Math.min(y,m))
}
