package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait SpriteMaterialParameters extends MaterialParameters {
  var color: Color | String | Double = js.native
  var map: Texture = js.native
  var rotation: Double = js.native
}