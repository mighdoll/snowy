package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("DirectionalLight")
class DirectionalLight protected () extends Light {
  def this(color: Color | String | Double = js.native, intensity: Double = js.native) = this()
  /**
     * Target used for shadow camera orientation.
     */
  var target: Object3D = js.native
  /**
     * Light's intensity.
     * Default — 1.0.
     */
  //var intensity: Double = js.native
  //var shadow: DirectionalLightShadow = js.native
}