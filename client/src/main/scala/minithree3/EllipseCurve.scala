package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("EllipseCurve")
class EllipseCurve protected () extends Curve[Vector2] {
  def this(aX: Double, aY: Double, xRadius: Double, yRadius: Double, aStartAngle: Double, aEndAngle: Double, aClockwise: Boolean, aRotation: Double) = this()
  var aX: Double = js.native
  var aY: Double = js.native
  var xRadius: Double = js.native
  var yRadius: Double = js.native
  var aStartAngle: Double = js.native
  var aEndAngle: Double = js.native
  var aClockwise: Boolean = js.native
  var aRotation: Double = js.native
}