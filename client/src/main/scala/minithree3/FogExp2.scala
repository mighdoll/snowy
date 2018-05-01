package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("FogExp2")
class FogExp2 protected () extends IFog {
  def this(hex: Double | String, density: Double = js.native) = this()
  var name: String = js.native
  var color: Color = js.native
  /**
     * Defines how fast the fog will grow dense.
     * Default is 0.00025.
     */
  var density: Double = js.native
  def toJSON(): js.Dynamic = js.native
}