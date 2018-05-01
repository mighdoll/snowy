package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("SpotLightHelper")
class SpotLightHelper protected () extends Object3D {
  def this(light: Light) = this()
  var light: Light = js.native
  def dispose(): Unit = js.native
  def update(): Unit = js.native
}