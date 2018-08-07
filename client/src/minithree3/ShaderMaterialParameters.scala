package minithree3

import scala.scalajs.js

@js.native
trait ShaderMaterialParameters extends MaterialParameters {
  var defines: js.Any = js.native
  var uniforms: js.Any = js.native
  var vertexShader: String = js.native
  var fragmentShader: String = js.native
  var lineWidth: Double = js.native
  var wireframe: Boolean = js.native
  var wireframeLinewidth: Double = js.native
  //var lights: Boolean = js.native
  var clipping: Boolean = js.native
  var skinning: Boolean = js.native
  var morphTargets: Boolean = js.native
  var morphNormals: Boolean = js.native
}