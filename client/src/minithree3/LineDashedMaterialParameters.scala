package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait LineDashedMaterialParameters extends MaterialParameters {
  var color: Color | String | Double = js.native
  var linewidth: Double = js.native
  var scale: Double = js.native
  var dashSize: Double = js.native
  var gapSize: Double = js.native
}