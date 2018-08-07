package minithree3

import org.scalajs.dom.ImageData

import scala.scalajs.js

  // returns and sets the value of Texture.image in the codde ?
@js.native
@js.annotation.JSGlobal("CompressedTexture")
class CompressedTexture protected () extends Texture {
  def this(mipmaps: js.Array[ImageData], width: Double, height: Double, format: PixelFormat = js.native, `type`: TextureDataType = js.native, mapping: Mapping = js.native, wrapS: Wrapping = js.native, wrapT: Wrapping = js.native, magFilter: TextureFilter = js.native, minFilter: TextureFilter = js.native, anisotropy: Double = js.native, encoding: TextureEncoding = js.native) = this()
    //var image: js.Any = js.native
}