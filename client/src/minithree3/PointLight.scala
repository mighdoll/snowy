package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("PointLight")
class PointLight protected () extends Light {
  def this(color: Color | String | Double = js.native, intensity: Double = js.native, distance: Double = js.native, decay: Double = js.native) = this()
  /*
        * Light's intensity.
        * Default - 1.0.
        */
  //var intensity: Double = js.native
  /**
     * If non-zero, light will attenuate linearly from maximum intensity at light position down to zero at distance.
     * Default — 0.0.
     */
  var distance: Double = js.native
  var decay: Double = js.native
  //var shadow: PointLightShadow = js.native
  var power: Double = js.native
}