package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ParametricGeometry")
class ParametricGeometry protected () extends Geometry {
  def this(func: js.Function2[Double, Double, Vector3], slices: Double, stacks: Double) = this()
  var parameters: js.Any = js.native
}