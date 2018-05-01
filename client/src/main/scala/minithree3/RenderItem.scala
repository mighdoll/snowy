package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait RenderItem extends js.Object {
  var id: Double = js.native
  var `object`: Object3D = js.native
  var geometry: Geometry | BufferGeometry = js.native
  var material: Material = js.native
  var program: WebGLProgram = js.native
  var renderOrder: Double = js.native
  var z: Double = js.native
  var group: Group = js.native
}