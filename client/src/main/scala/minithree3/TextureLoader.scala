package minithree3

import org.scalajs.dom.{ErrorEvent, ProgressEvent}

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("TextureLoader")
class TextureLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  var crossOrigin: String = js.native
  var withCredentials: String = js.native
  var path: String = js.native
  /**
     * Begin loading from url
     *
     * @param url
     */
  def load(url: String, onLoad: js.Function1[Texture, Unit] = js.native, onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): Texture = js.native
  def setCrossOrigin(crossOrigin: String): TextureLoader = js.native
  def setWithCredentials(value: String): TextureLoader = js.native
  def setPath(path: String): TextureLoader = js.native
}