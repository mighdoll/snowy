package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLPrograms")
class WebGLPrograms protected () extends js.Object {
  def this(renderer: WebGLRenderer, capabilities: js.Any) = this()
  var programs: js.Array[js.Any] = js.native
  def getParameters(material: ShaderMaterial, lights: js.Any, fog: js.Any, nClipPlanes: Double, `object`: js.Any): js.Dynamic = js.native
  def getProgramCode(material: ShaderMaterial, parameters: js.Any): String = js.native
  def acquireProgram(material: ShaderMaterial, parameters: js.Any, code: String): WebGLProgram = js.native
  def releaseProgram(program: WebGLProgram): Unit = js.native
}