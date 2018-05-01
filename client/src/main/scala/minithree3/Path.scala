package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Path")
class Path protected () extends CurvePath[Vector2] {
  def this(points: js.Array[Vector2] = js.native) = this()
  var currentPoint: Vector2 = js.native
  /**
     * @deprecated Use { Path#setFromPoints .setFromPoints()} instead.
     */
  def fromPoints(vectors: js.Array[Vector2]): Unit = js.native
  def setFromPoints(vectors: js.Array[Vector2]): Unit = js.native
  def moveTo(x: Double, y: Double): Unit = js.native
  def lineTo(x: Double, y: Double): Unit = js.native
  def quadraticCurveTo(aCPx: Double, aCPy: Double, aX: Double, aY: Double): Unit = js.native
  def bezierCurveTo(aCP1x: Double, aCP1y: Double, aCP2x: Double, aCP2y: Double, aX: Double, aY: Double): Unit = js.native
  def splineThru(pts: js.Array[Vector2]): Unit = js.native
  def arc(aX: Double, aY: Double, aRadius: Double, aStartAngle: Double, aEndAngle: Double, aClockwise: Boolean): Unit = js.native
  def absarc(aX: Double, aY: Double, aRadius: Double, aStartAngle: Double, aEndAngle: Double, aClockwise: Boolean): Unit = js.native
  def ellipse(aX: Double, aY: Double, xRadius: Double, yRadius: Double, aStartAngle: Double, aEndAngle: Double, aClockwise: Boolean, aRotation: Double): Unit = js.native
  def absellipse(aX: Double, aY: Double, xRadius: Double, yRadius: Double, aStartAngle: Double, aEndAngle: Double, aClockwise: Boolean, aRotation: Double): Unit = js.native
}