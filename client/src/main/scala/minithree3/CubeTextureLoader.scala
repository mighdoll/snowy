package minithree3

import org.scalajs.dom.{ErrorEvent, ProgressEvent}

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CubeTextureLoader")
class CubeTextureLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  var crossOrigin: String = js.native
  var path: String = js.native
  def load(urls: js.Array[String], onLoad: js.Function1[CubeTexture, Unit] = js.native, onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): CubeTexture = js.native
  def setCrossOrigin(crossOrigin: String): CubeTextureLoader = js.native
  def setPath(path: String): CubeTextureLoader = js.native
}