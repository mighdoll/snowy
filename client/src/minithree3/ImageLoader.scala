package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
@js.annotation.JSGlobal("ImageLoader")
class ImageLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  var crossOrigin: String = js.native
  var withCredentials: String = js.native
  var path: String = js.native
  /**
     * Begin loading from url
     * @param url
     */
  def load(url: String, onLoad: js.Function1[HTMLImageElement, Unit] = js.native, onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): HTMLImageElement = js.native
  def setCrossOrigin(crossOrigin: String): ImageLoader = js.native
  def setWithCredentials(value: String): ImageLoader = js.native
  def setPath(value: String): ImageLoader = js.native
}