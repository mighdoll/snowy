package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Font")
class Font protected () extends js.Object {
  def this(jsondata: String) = this()
  var data: String = js.native
  def generateShapes(text: String, size: Double, divisions: Double): js.Array[js.Any] = js.native
}