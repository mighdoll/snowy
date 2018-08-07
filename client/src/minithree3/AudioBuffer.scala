package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("AudioBuffer")
class AudioBuffer protected () extends js.Object {
  def this(context: js.Any) = this()
  var context: js.Any = js.native
  var ready: Boolean = js.native
  var readyCallbacks: js.Array[js.Function] = js.native
  def load(file: String): AudioBuffer = js.native
  def onReady(callback: js.Function): Unit = js.native
}