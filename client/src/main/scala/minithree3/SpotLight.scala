package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("SpotLight")
class SpotLight protected () extends Light {
  def this(color: Color | String | Double = js.native, intensity: Double = js.native, distance: Double = js.native, angle: Double = js.native, exponent: Double = js.native, decay: Double = js.native) = this()
  /**
     * Spotlight focus points at target.position.
     * Default position — (0,0,0).
     */
  var target: Object3D = js.native
  /**
     * Light's intensity.
     * Default — 1.0.
     */
  //var intensity: Double = js.native
  /**
     * If non-zero, light will attenuate linearly from maximum intensity at light position down to zero at distance.
     * Default — 0.0.
     */
  var distance: Double = js.native
  /*
        * Maximum extent of the spotlight, in radians, from its direction.
        * Default — Math.PI/2.
        */
  var angle: Double = js.native
  /**
     * Rapidity of the falloff of light from its target direction.
     * Default — 10.0.
     */
  var exponent: Double = js.native
  var decay: Double = js.native
  //var shadow: SpotLightShadow = js.native
  var power: Double = js.native
  var penumbra: Double = js.native
}