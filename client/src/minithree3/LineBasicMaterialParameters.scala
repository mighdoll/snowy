package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait LineBasicMaterialParameters extends MaterialParameters {
  var color: Color | String | Double = js.native
  var linewidth: Double = js.native
  var linecap: String = js.native
  var linejoin: String = js.native
}