package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLUniforms")
class WebGLUniforms protected () extends js.Object {
  def this(gl: js.Any, program: WebGLProgram, renderer: WebGLRenderer) = this()
  var renderer: WebGLRenderer = js.native
  def setValue(gl: js.Any, value: js.Any, renderer: js.Any = js.native): Unit = js.native
  def set(gl: js.Any, `object`: js.Any, name: String): Unit = js.native
  def setOptional(gl: js.Any, `object`: js.Any, name: String): Unit = js.native
}

@js.native
@js.annotation.JSGlobal("WebGLUniforms")
object WebGLUniforms extends js.Object {
  def upload(gl: js.Any, seq: js.Any, values: js.Array[js.Any], renderer: js.Any): Unit = js.native
  def seqWithValue(seq: js.Any, values: js.Array[js.Any]): js.Array[js.Any] = js.native
  def splitDynamic(seq: js.Any, values: js.Array[js.Any]): js.Array[js.Any] = js.native
  def evalDynamic(seq: js.Any, values: js.Array[js.Any], `object`: js.Any, camera: js.Any): js.Array[js.Any] = js.native
}