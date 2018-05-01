package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CatmullRomCurve3")
class CatmullRomCurve3 protected () extends Curve[Vector3] {
  def this(points: js.Array[Vector3] = js.native) = this()
  var points: js.Array[Vector3] = js.native
  def getPoint(t: Double): Vector3 = js.native
}