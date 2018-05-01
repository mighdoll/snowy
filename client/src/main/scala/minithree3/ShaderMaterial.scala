package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ShaderMaterial")
class ShaderMaterial protected () extends Material {
  def this(parameters: ShaderMaterialParameters = js.native) = this()
  var defines: js.Any = js.native
  var uniforms: js.Dictionary[IUniform] = js.native
  var vertexShader: String = js.native
  var fragmentShader: String = js.native
  var linewidth: Double = js.native
  var wireframe: Boolean = js.native
  var wireframeLinewidth: Double = js.native
  var lights: Boolean = js.native
  var clipping: Boolean = js.native
  var skinning: Boolean = js.native
  var morphTargets: Boolean = js.native
  var morphNormals: Boolean = js.native
  /**
     * @deprecated Use { ShaderMaterial#extensions.derivatives extensions.derivatives} instead.
     */
  var derivatives: js.Any = js.native
  var extensions: js.Any = js.native
  var defaultAttributeValues: js.Any = js.native
  var index0AttributeName: String = js.native
  def setValues(parameters: ShaderMaterialParameters): Unit = js.native
  def toJSON(meta: js.Any): js.Dynamic = js.native
}