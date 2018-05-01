package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("FontLoader")
class FontLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  def load(url: String, onLoad: js.Function1[Font, Unit] = js.native, onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): Unit = js.native
  def parse(json: String): Font = js.native
}