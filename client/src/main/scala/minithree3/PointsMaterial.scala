package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("PointsMaterial")
class PointsMaterial protected () extends Material {
  def this(parameters: PointsMaterialParameters = js.native) = this()
  var color: Color = js.native
  var map: Texture = js.native
  var size: Double = js.native
  var sizeAttenuation: Boolean = js.native
  def setValues(parameters: PointsMaterialParameters): Unit = js.native
}