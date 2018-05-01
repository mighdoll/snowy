package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LineBasicMaterial")
class LineBasicMaterial protected () extends Material {
  def this(parameters: LineBasicMaterialParameters = js.native) = this()
  var color: Color = js.native
  var linewidth: Double = js.native
  var linecap: String = js.native
  var linejoin: String = js.native
  def setValues(parameters: LineBasicMaterialParameters): Unit = js.native
}