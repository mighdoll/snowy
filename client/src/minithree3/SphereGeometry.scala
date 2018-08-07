package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("SphereGeometry")
class SphereGeometry protected () extends Geometry {
  def this(radius: Double, widthSegments: Double = js.native, heightSegments: Double = js.native, phiStart: Double = js.native, phiLength: Double = js.native, thetaStart: Double = js.native, thetaLength: Double = js.native) = this()
  /**
     * The geometry is created by sweeping and calculating vertexes around the Y axis (horizontal sweep) and the Z axis (vertical sweep). Thus, incomplete spheres (akin to 'sphere slices') can be created through the use of different values of phiStart, phiLength, thetaStart and thetaLength, in order to define the points in which we start (or end) calculating those vertices.
     *
     * @param radius — sphere radius. Default is 50.
     * @param widthSegments — number of horizontal segments. Minimum value is 3, and the default is 8.
     * @param heightSegments — number of vertical segments. Minimum value is 2, and the default is 6.
     * @param phiStart — specify horizontal starting angle. Default is 0.
     * @param phiLength — specify horizontal sweep angle size. Default is Math.PI * 2.
     * @param thetaStart — specify vertical starting angle. Default is 0.
     * @param thetaLength — specify vertical sweep angle size. Default is Math.PI.
     */
  var parameters: js.Any = js.native
}