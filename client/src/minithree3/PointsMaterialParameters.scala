package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait PointsMaterialParameters extends MaterialParameters {
  var color: Color | String | Double = js.native
  var map: Texture = js.native
  var size: Double = js.native
  var sizeAttenuation: Boolean = js.native
}