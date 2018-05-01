package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("TextBufferGeometry")
class TextBufferGeometry protected () extends ExtrudeBufferGeometry {
  def this(text: String, parameters: TextGeometryParameters = js.native) = this()
  var parameters: js.Any = js.native
}