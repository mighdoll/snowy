package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("SpotLightShadow")
class SpotLightShadow extends LightShadow {
  var camera: PerspectiveCamera = js.native
  def update(light: Light): Unit = js.native
}