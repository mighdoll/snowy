package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CylinderGeometry")
class CylinderGeometry protected () extends Geometry {
  def this(radiusTop: Double = js.native, radiusBottom: Double = js.native, height: Double = js.native, radiusSegments: Double = js.native, heightSegments: Double = js.native, openEnded: Boolean = js.native, thetaStart: Double = js.native, thetaLength: Double = js.native) = this()
  /**
     * @param radiusTop — Radius of the cylinder at the top.
     * @param radiusBottom — Radius of the cylinder at the bottom.
     * @param height — Height of the cylinder.
     * @param radiusSegments — Number of segmented faces around the circumference of the cylinder.
     * @param heightSegments — Number of rows of faces along the height of the cylinder.
     * @param openEnded - A Boolean indicating whether or not to cap the ends of the cylinder.
     */
  var parameters: js.Any = js.native
}