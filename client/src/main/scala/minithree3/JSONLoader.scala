package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("JSONLoader")
class JSONLoader protected () extends Loader {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  var withCredentials: Boolean = js.native
  def load(url: String, onLoad: js.Function2[Geometry, js.Array[Material], Unit] = js.native, onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): Unit = js.native
  def setTexturePath(value: String): Unit = js.native
  def parse(json: js.Any, texturePath: String = js.native): js.Any = js.native
}