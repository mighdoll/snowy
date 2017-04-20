package snowy.playfield
import vector.Vec2d

/** a rectangle
  *
  * @param pos location of top left
  * @param size width and height
  */
case class Rect(pos: Vec2d, size: Vec2d) {
  def left   = pos.x
  def right  = pos.x + size.x
  def top    = pos.y
  def bottom = pos.y + size.y
}

object Rect {
  def apply(left: Double, top: Double, right: Double, bottom: Double): Rect =
    Rect(Vec2d(left, top), Vec2d(right - left, bottom - top))
}

case class Circle(pos: Vec2d, radius: Double)
