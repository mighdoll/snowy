package minithree3

import org.scalajs.dom.{ErrorEvent, ProgressEvent}

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("MaterialLoader")
class MaterialLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  var textures: js.Dictionary[Texture] = js.native
  def load(url: String, onLoad: js.Function1[Material, Unit], onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[Error | ErrorEvent, Unit] = js.native): Unit = js.native
  def setTextures(textures: js.Dictionary[Texture]): Unit = js.native
  def getTexture(name: String): Texture = js.native
  def parse(json: js.Any): Material = js.native
}