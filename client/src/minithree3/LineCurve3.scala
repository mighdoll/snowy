package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LineCurve3")
class LineCurve3 protected () extends Curve[Vector3] {
  def this(v1: Vector3, v2: Vector3) = this()
  var v1: Vector3 = js.native
  var v2: Vector3 = js.native
  def getPoint(t: Double): Vector3 = js.native
}