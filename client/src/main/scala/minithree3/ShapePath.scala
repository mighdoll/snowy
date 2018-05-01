package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ShapePath")
class ShapePath extends js.Object {
  var subPaths: js.Array[js.Any] = js.native
  var currentPath: js.Any = js.native
  def moveTo(x: Double, y: Double): Unit = js.native
  def lineTo(x: Double, y: Double): Unit = js.native
  def quadraticCurveTo(aCPx: Double, aCPy: Double, aX: Double, aY: Double): Unit = js.native
  def bezierCurveTo(aCP1x: Double, aCP1y: Double, aCP2x: Double, aCP2y: Double, aX: Double, aY: Double): Unit = js.native
  def splineThru(pts: js.Array[Vector2]): Unit = js.native
  def toShapes(isCCW: Boolean, noHoles: js.Any): js.Array[Shape] = js.native
}