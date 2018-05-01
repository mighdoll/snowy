package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ArrayCamera")
class ArrayCamera protected () extends PerspectiveCamera {
  def this(cameras: js.Array[PerspectiveCamera] = js.native) = this()
  var cameras: js.Array[PerspectiveCamera] = js.native
  var isArrayCamera: Boolean = js.native
}