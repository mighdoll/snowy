package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CubicBezierCurve3")
class CubicBezierCurve3 protected () extends Curve[Vector3] {
  def this(v0: Vector3, v1: Vector3, v2: Vector3, v3: Vector3) = this()
  var v0: Vector3 = js.native
  var v1: Vector3 = js.native
  var v2: Vector3 = js.native
  var v3: Vector3 = js.native
  def getPoint(t: Double): Vector3 = js.native
}