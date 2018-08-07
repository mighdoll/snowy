package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ArcCurve")
class ArcCurve protected () extends EllipseCurve {
  def this(aX: Double, aY: Double, aRadius: Double, aStartAngle: Double, aEndAngle: Double, aClockwise: Boolean) = this()
}