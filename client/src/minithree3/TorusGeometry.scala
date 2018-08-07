package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("TorusGeometry")
class TorusGeometry protected () extends Geometry {
  def this(radius: Double = js.native, tube: Double = js.native, radialSegments: Double = js.native, tubularSegments: Double = js.native, arc: Double = js.native) = this()
  var parameters: js.Any = js.native
}