package minithree3

import scala.scalajs.js

@js.native
trait MeshNormalMaterialParameters extends MaterialParameters {
  /** Render geometry as wireframe. Default is false (i.e. render as smooth shaded). */
  var wireframe: Boolean = js.native
  /** Controls wireframe thickness. Default is 1. */
  var wireframeLinewidth: Double = js.native
  var morphTargets: Boolean = js.native
}