package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("DataTextureLoader")
class DataTextureLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  def load(url: String, onLoad: js.Function1[DataTexture, Unit], onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): Unit = js.native
}