package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("PlaneGeometry")
class PlaneGeometry protected () extends Geometry {
  def this(width: Double, height: Double, widthSegments: Double = js.native, heightSegments: Double = js.native) = this()
  var parameters: js.Any = js.native
}