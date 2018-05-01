package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ShaderLib")
object ShaderLib extends js.Object {
  @js.annotation.JSBracketAccess
  def apply(name: String): Shader = js.native
  @js.annotation.JSBracketAccess
  def update(name: String, v: Shader): Unit = js.native
  var basic: Shader = js.native
  var lambert: Shader = js.native
  var phong: Shader = js.native
  var standard: Shader = js.native
  var points: Shader = js.native
  var dashed: Shader = js.native
  var depth: Shader = js.native
  var normal: Shader = js.native
  var cube: Shader = js.native
  var equirect: Shader = js.native
  var depthRGBA: Shader = js.native
  var distanceRGBA: Shader = js.native
  var physical: Shader = js.native
}