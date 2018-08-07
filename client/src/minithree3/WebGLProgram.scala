package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLProgram")
class WebGLProgram protected () extends js.Object {
  def this(renderer: WebGLRenderer, code: String, material: ShaderMaterial, parameters: WebGLRendererParameters) = this()
  var id: Double = js.native
  var code: String = js.native
  var usedTimes: Double = js.native
  var program: js.Any = js.native
  var vertexShader: WebGLShader = js.native
  var fragmentShader: WebGLShader = js.native
  /**
     * @deprecated Use { WebGLProgram#getUniforms getUniforms()} instead.
     */
  var uniforms: js.Any = js.native
  /**
     * @deprecated Use { WebGLProgram#getAttributes getAttributes()} instead.
     */
  var attributes: js.Any = js.native
  def getUniforms(): WebGLUniforms = js.native
  def getAttributes(): js.Dynamic = js.native
  def destroy(): Unit = js.native
}