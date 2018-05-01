package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("AudioLoader")
class AudioLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  def load(url: String, onLoad: js.Function, onPrgress: js.Function, onError: js.Function): Unit = js.native
}