package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("DepthTexture")
class DepthTexture protected () extends Texture {
  def this(width: Double, heighht: Double, `type`: TextureDataType = js.native, mapping: Mapping = js.native, wrapS: Wrapping = js.native, wrapT: Wrapping = js.native, magFilter: TextureFilter = js.native, minFilter: TextureFilter = js.native, anisotropy: Double = js.native) = this()
  var image: js.Any = js.native
}