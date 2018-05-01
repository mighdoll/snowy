package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LineDashedMaterial")
class LineDashedMaterial protected () extends Material {
  def this(parameters: LineDashedMaterialParameters = js.native) = this()
  var color: Color = js.native
  var linewidth: Double = js.native
  var scale: Double = js.native
  var dashSize: Double = js.native
  var gapSize: Double = js.native
  def setValues(parameters: LineDashedMaterialParameters): Unit = js.native
}