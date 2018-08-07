package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("SpriteMaterial")
class SpriteMaterial protected () extends Material {
  def this(parameters: SpriteMaterialParameters = js.native) = this()
  var color: Color = js.native
  var map: Texture = js.native
  var rotation: Double = js.native
  def setValues(parameters: SpriteMaterialParameters): Unit = js.native
}