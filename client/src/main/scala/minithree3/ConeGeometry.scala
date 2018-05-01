package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ConeGeometry")
class ConeGeometry protected () extends CylinderGeometry {
  def this(radius: Double = js.native, height: Double = js.native, radialSegment: Double = js.native, heightSegment: Double = js.native, openEnded: Boolean = js.native, thetaStart: Double = js.native, thetaLength: Double = js.native) = this()
}