package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("HemisphereLight")
class HemisphereLight protected () extends Light {
  def this(skyColor: Color | String | Double = js.native, groundColor: Color | String | Double = js.native, intensity: Double = js.native) = this()
  var skyColor: Color = js.native
  var groundColor: Color = js.native
  //var intensity: Double = js.native
}