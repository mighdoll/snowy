package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Uniform")
class Uniform protected () extends js.Object {
  def this(value: js.Any) = this()
  def this(`type`: String, value: js.Any) = this()
  /**
     * @deprecated
     */
  /**
     * @deprecated
     */
  var `type`: String = js.native
  var value: js.Any = js.native
  /**
     * @deprecated Use { Object3D#onBeforeRender object.onBeforeRender()} instead.
     */
  var dynamic: Boolean = js.native
  var onUpdateCallback: js.Function = js.native
  /**
     * @deprecated Use { Object3D#onBeforeRender object.onBeforeRender()} instead.
     */
  def onUpdate(callback: js.Function): Uniform = js.native
}