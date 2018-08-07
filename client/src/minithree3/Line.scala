package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("Line")
class Line protected () extends Object3D {
  def this(geometry: Geometry | BufferGeometry = js.native, material: LineDashedMaterial | LineBasicMaterial | ShaderMaterial = js.native, mode: Double = js.native) = this()
  var geometry: Geometry | BufferGeometry = js.native
  var material: Material = js.native
  // LineDashedMaterial or LineBasicMaterial or ShaderMaterial
  def computeLineDistances(): js.Dynamic = js.native
  //def raycast(raycaster: Raycaster, intersects: js.Any): Unit = js.native
}