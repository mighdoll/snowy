package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("FileLoader")
class FileLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  var mimeType: MimeType = js.native
  var path: String = js.native
  var responseType: String = js.native
  var withCredentials: String = js.native
  def load(url: String, onLoad: js.Function1[String, Unit] = js.native, onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): js.Dynamic = js.native
  def setMimeType(mimeType: MimeType): FileLoader = js.native
  def setPath(path: String): FileLoader = js.native
  def setResponseType(responseType: String): FileLoader = js.native
  def setWithCredentials(value: String): FileLoader = js.native
}