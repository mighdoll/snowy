package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("MeshNormalMaterial")
class MeshNormalMaterial protected () extends Material {
  def this(parameters: MeshNormalMaterialParameters = js.native) = this()
  var wireframe: Boolean = js.native
  var wireframeLinewidth: Double = js.native
  var morphTargets: Boolean = js.native
  def setValues(parameters: MeshNormalMaterialParameters): Unit = js.native
}