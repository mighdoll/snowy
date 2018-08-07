package minithree3.ShapeUtils

import scala.scalajs.js
@js.native
@js.annotation.JSGlobal("ShapeUtils")
object ShapeUtils extends js.Object {
  def area(contour: js.Array[Double]): Double = js.native
  def triangulate(contour: js.Array[Double], indices: Boolean): js.Array[Double] = js.native
  def triangulateShape(contour: js.Array[Double], holes: js.Array[js.Any]): js.Array[Double] = js.native
  def isClockWise(pts: js.Array[Double]): Boolean = js.native
}