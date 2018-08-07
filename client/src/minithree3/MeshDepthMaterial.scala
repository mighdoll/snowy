package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("MeshDepthMaterial")
class MeshDepthMaterial protected () extends Material {
  def this(parameters: MeshDepthMaterialParameters = js.native) = this()
  var wireframe: Boolean = js.native
  var wireframeLinewidth: Double = js.native
  def setValues(parameters: MeshDepthMaterialParameters): Unit = js.native
}