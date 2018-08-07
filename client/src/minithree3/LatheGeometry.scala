package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LatheGeometry")
class LatheGeometry protected () extends Geometry {
  def this(points: js.Array[Vector2], segments: Double = js.native, phiStart: Double = js.native, phiLength: Double = js.native) = this()
  var parameters: js.Any = js.native
}