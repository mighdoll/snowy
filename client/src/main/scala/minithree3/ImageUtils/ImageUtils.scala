package minithree3.ImageUtils

import scala.scalajs.js
@js.native
@js.annotation.JSGlobal("ImageUtils")
object ImageUtils extends js.Object {
  /**
     * @deprecated
     */
  val crossOrigin: String = js.native
  /**
     * @deprecated Use { TextureLoader THREE.TextureLoader()} instead.
     */
  def loadTexture(url: String, mapping: Mapping = js.native, onLoad: js.Function1[Texture, Unit] = js.native, onError: js.Function1[String, Unit] = js.native): Texture = js.native
  /**
     * @deprecated Use { CubeTextureLoader THREE.CubeTextureLoader()} instead.
     */
  def loadTextureCube(array: js.Array[String], mapping: Mapping = js.native, onLoad: js.Function1[Texture, Unit] = js.native, onError: js.Function1[String, Unit] = js.native): Texture = js.native
}