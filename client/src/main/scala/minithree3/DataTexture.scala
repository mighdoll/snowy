package minithree3

import scala.scalajs.js
import scala.scalajs.js.|
import scala.scalajs.js.typedarray._

@js.native
@js.annotation.JSGlobal("DataTexture")
class DataTexture protected () extends Texture {
  def this(data: ArrayBuffer | Int8Array | Uint8Array | Uint8ClampedArray | Int16Array | Uint16Array | Int32Array | Uint32Array | Float32Array | Float64Array, width: Double, height: Double, format: PixelFormat = js.native, `type`: TextureDataType = js.native, mapping: Mapping = js.native, wrapS: Wrapping = js.native, wrapT: Wrapping = js.native, magFilter: TextureFilter = js.native, minFilter: TextureFilter = js.native, anisotropy: Double = js.native, encoding: TextureEncoding = js.native) = this()
  var image: ImageData = js.native
}