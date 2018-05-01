package minithree3

import scala.scalajs.js

@js.native
trait Shader extends js.Object {
  var uniforms: js.Dictionary[IUniform] = js.native
  var vertexShader: String = js.native
  var fragmentShader: String = js.native
}