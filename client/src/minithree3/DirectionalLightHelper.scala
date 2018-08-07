package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("DirectionalLightHelper")
class DirectionalLightHelper protected () extends Object3D {
  def this(light: Light, size: Double = js.native) = this()
  var light: Light = js.native
  var lightPlane: Line = js.native
  def dispose(): Unit = js.native
  def update(): Unit = js.native
}