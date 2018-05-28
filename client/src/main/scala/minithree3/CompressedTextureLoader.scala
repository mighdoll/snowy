package minithree3

import org.scalajs.dom.{ErrorEvent, ProgressEvent}

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CompressedTextureLoader")
class CompressedTextureLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  var path: String = js.native
  def load(url: String, onLoad: js.Function1[CompressedTexture, Unit], onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): Unit = js.native
  def setPath(path: String): CompressedTextureLoader = js.native
}