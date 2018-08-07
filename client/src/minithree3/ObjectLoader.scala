package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("ObjectLoader")
class ObjectLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  var texturePass: String = js.native
  var crossOrigin: String = js.native
  def load(url: String, onLoad: js.Function1[Object3D, Unit] = js.native, onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[Error | ErrorEvent, Unit] = js.native): Unit = js.native
  def setTexturePath(value: String): Unit = js.native
  def setCrossOrigin(crossOrigin: String): Unit = js.native
  def parse[T <: Object3D](json: js.Any, onLoad: js.Function1[Object3D, Unit] = js.native): T = js.native
  def parseGeometries(json: js.Any): js.Array[js.Any] = js.native
  // Array of BufferGeometry or Geometry or Geometry2.
  def parseMaterials(json: js.Any, textures: js.Array[Texture]): js.Array[Material] = js.native
  // Array of Classes that inherits from Matrial.
  def parseAnimations(json: js.Any): js.Array[AnimationClip] = js.native
  def parseImages(json: js.Any, onLoad: js.Function0[Unit]): js.Dictionary[HTMLImageElement] = js.native
  def parseTextures(json: js.Any, images: js.Any): js.Array[Texture] = js.native
  def parseObject[T <: Object3D](data: js.Any, geometries: js.Array[js.Any], materials: js.Array[Material]): T = js.native
}