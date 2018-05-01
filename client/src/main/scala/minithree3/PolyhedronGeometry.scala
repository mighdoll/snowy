package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("PolyhedronGeometry")
class PolyhedronGeometry protected () extends Geometry {
  def this(vertices: js.Array[Double], indices: js.Array[Double], radius: Double = js.native, detail: Double = js.native) = this()
  var parameters: js.Any = js.native
  var boundingSphere: Sphere = js.native
}