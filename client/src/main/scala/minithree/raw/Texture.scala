package minithree.raw

import org.scalajs.dom.ImageData

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSBracketAccess, JSName}

@js.native
@JSName("THREE.Texture")
class Texture(
      var image: js.Any,
      var mapping: Mapping = js.native,
      var wrapS: Wrapping = js.native,
      var wrapT: Wrapping = js.native,
      var magFilter: TextureFilter = js.native,
      var minFilter: TextureFilter = js.native,
      var format: PixelFormat = js.native,
      var `type`: TextureDataType = js.native,
      var anisotropy: Double = js.native
) extends js.Object with EventDispatcher {
  var id: Double                   = js.native
  var uuid: String                 = js.native
  var name: String                 = js.native
  var mipmaps: js.Array[ImageData] = js.native
  var offset: Vector2              = js.native
  var repeat: Vector2              = js.native
  var generateMipmaps: Boolean     = js.native
  var premultiplyAlpha: Boolean    = js.native
  var flipY: Boolean               = js.native
  var unpackAlignment: Double      = js.native
  var needsUpdate: Boolean         = js.native
  var onUpdate: js.Function0[Unit] = js.native
  override def clone(): Texture    = js.native
  def update(): Unit               = js.native
  def dispose(): Unit              = js.native
}

@js.native
@JSName("THREE.Texture")
object Texture extends js.Object {
  var DEFAULT_IMAGE: js.Any   = js.native
  var DEFAULT_MAPPING: js.Any = js.native
}

@js.native
sealed trait TextureDataType extends js.Object {}

@js.native
@JSName("THREE.TextureDataType")
object TextureDataType extends js.Object {
  @JSBracketAccess
  def apply(value: TextureDataType): String = js.native
}

@js.native
sealed trait TextureFilter extends js.Object {}

@js.native
@JSName("THREE.TextureFilter")
object TextureFilter extends js.Object {
  @JSBracketAccess
  def apply(value: TextureFilter): String = js.native
}
