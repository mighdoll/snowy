package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("RingGeometry")
class RingGeometry protected () extends Geometry {
  def this(innerRadius: Double = js.native, outerRadius: Double = js.native, thetaSegments: Double = js.native, phiSegments: Double = js.native, thetaStart: Double = js.native, thetaLength: Double = js.native) = this()
  var parameters: js.Any = js.native
}