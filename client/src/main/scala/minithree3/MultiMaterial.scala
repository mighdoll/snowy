package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("MultiMaterial")
class MultiMaterial protected () extends Material {
  def this(materials: js.Array[Material] = js.native) = this()
  var isMultiMaterial: Boolean = js.native
  var materials: js.Array[Material] = js.native
  //def toJSON(meta: js.Any): js.Dynamic = js.native
}