package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("BoxGeometry")
class BoxGeometry protected () extends Geometry {
  def this(width: Double, height: Double, depth: Double, widthSegments: Double = js.native, heightSegments: Double = js.native, depthSegments: Double = js.native) = this()
  /**
     * @param width — Width of the sides on the X axis.
     * @param height — Height of the sides on the Y axis.
     * @param depth — Depth of the sides on the Z axis.
     * @param widthSegments — Number of segmented faces along the width of the sides.
     * @param heightSegments — Number of segmented faces along the height of the sides.
     * @param depthSegments — Number of segmented faces along the depth of the sides.
     */
  var parameters: js.Any = js.native
}