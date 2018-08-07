package minithree3

import scala.scalajs.js

@js.native
trait MeshDepthMaterialParameters extends MaterialParameters {
  var wireframe: Boolean = js.native
  var wireframeLinewidth: Double = js.native
}