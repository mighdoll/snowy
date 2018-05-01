package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("WebGLRenderList")
class WebGLRenderList extends js.Object {
  var opaque: js.Array[RenderItem] = js.native
  var transparent: js.Array[js.Any] = js.native
  def init(): Unit = js.native
  def push(`object`: Object3D, geometry: Geometry | BufferGeometry, material: Material, z: Double, group: Group): Unit = js.native
  def sort(): Unit = js.native
}