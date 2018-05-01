package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("Texture")
class Texture protected () extends EventDispatcher {
  def this(image: HTMLImageElement | HTMLCanvasElement | HTMLVideoElement = js.native, mapping: Mapping = js.native, wrapS: Wrapping = js.native, wrapT: Wrapping = js.native, magFilter: TextureFilter = js.native, minFilter: TextureFilter = js.native, format: PixelFormat = js.native, `type`: TextureDataType = js.native, anisotropy: Double = js.native, encoding: TextureEncoding = js.native) = this()
  var id: Double = js.native
  var uuid: String = js.native
  var name: String = js.native
  var sourceFile: String = js.native
  var image: js.Any = js.native
  // HTMLImageElement or ImageData or { width: number, height: number } in some children;
  var mipmaps: js.Array[ImageData] = js.native
  var mapping: Mapping = js.native
  var wrapS: Wrapping = js.native
  var wrapT: Wrapping = js.native
  var magFilter: TextureFilter = js.native
  var minFilter: TextureFilter = js.native
  var anisotropy: Double = js.native
  var format: PixelFormat = js.native
  var `type`: TextureDataType = js.native
  var offset: Vector2 = js.native
  var repeat: Vector2 = js.native
  var center: Vector2 = js.native
  var rotation: Double = js.native
  var generateMipmaps: Boolean = js.native
  var premultiplyAlpha: Boolean = js.native
  var flipY: Boolean = js.native
  var unpackAlignment: Double = js.native
  var encoding: TextureEncoding = js.native
  var version: Double = js.native
  var needsUpdate: Boolean = js.native
  var onUpdate: js.Function0[Unit] = js.native
  def copy(source: Texture): js.Dynamic = js.native
  def toJSON(meta: js.Any): js.Dynamic = js.native
  def dispose(): Unit = js.native
  def transformUv(uv: Vector): Unit = js.native
}

@js.native
@js.annotation.JSGlobal("Texture")
object Texture extends js.Object {
  val DEFAULT_IMAGE: js.Any = js.native
  val DEFAULT_MAPPING: js.Any = js.native
}