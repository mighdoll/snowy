package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("TextGeometry")
class TextGeometry protected () extends ExtrudeGeometry {
  def this(text: String, parameters: TextGeometryParameters = js.native) = this()
  var parameters: js.Any = js.native
}