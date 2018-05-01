package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("Points")
class Points protected () extends Object3D {
  def this(geometry: Geometry | BufferGeometry = js.native, material: Material = js.native) = this()
  /**
     * @param geometry An instance of Geometry or BufferGeometry.
     * @param material An instance of Material (optional).
     */
  /**
     * An instance of Geometry or BufferGeometry, where each vertex designates the position of a particle in the system.
     */
  var geometry: Geometry | BufferGeometry = js.native
  /**
     * An instance of Material, defining the object's appearance. Default is a PointsMaterial with randomised colour.
     */
  var material: Material = js.native
  def raycast(raycaster: Raycaster, intersects: js.Any): Unit = js.native
}