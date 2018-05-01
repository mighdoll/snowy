package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CircleGeometry")
class CircleGeometry protected () extends Geometry {
  def this(radius: Double = js.native, segments: Double = js.native, thetaStart: Double = js.native, thetaLength: Double = js.native) = this()
  var parameters: js.Any = js.native
}