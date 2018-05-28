package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("AmbientLight")
class AmbientLight protected () extends Light {
  def this(color: Color | String | Double = js.native, intensity: Double = js.native) = this()
  /**
     * This creates a Ambientlight with a color.
     * @param color Numeric value of the RGB component of the color or a Color instance.
     */
  //var castShadow: Boolean = js.native
}