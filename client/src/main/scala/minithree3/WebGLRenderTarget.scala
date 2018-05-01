package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

  // true;
@js.native
@js.annotation.JSGlobal("WebGLRenderTarget")
class WebGLRenderTarget protected () extends EventDispatcher {
  def this(width: Double, height: Double, options: WebGLRenderTargetOptions = js.native) = this()
  var uuid: String = js.native
  var width: Double = js.native
  var height: Double = js.native
  var scissor: Vector4 = js.native
  var scissorTest: Boolean = js.native
  var viewport: Vector4 = js.native
  var texture: Texture = js.native
  var depthBuffer: Boolean = js.native
  var stencilBuffer: Boolean = js.native
  var depthTexture: Texture = js.native
  /**
     * @deprecated Use { Texture#wrapS texture.wrapS} instead.
     */
  var wrapS: js.Any = js.native
  /**
     * @deprecated Use { Texture#wrapT texture.wrapT} instead.
     */
  var wrapT: js.Any = js.native
  /**
     * @deprecated Use { Texture#magFilter texture.magFilter} instead.
     */
  var magFilter: js.Any = js.native
  /**
     * @deprecated Use { Texture#minFilter texture.minFilter} instead.
     */
  var minFilter: js.Any = js.native
  /**
     * @deprecated Use { Texture#anisotropy texture.anisotropy} instead.
     */
  var anisotropy: js.Any = js.native
  /**
     * @deprecated Use { Texture#offset texture.offset} instead.
     */
  var offset: js.Any = js.native
  /**
     * @deprecated Use { Texture#repeat texture.repeat} instead.
     */
  var repeat: js.Any = js.native
  /**
     * @deprecated Use { Texture#format texture.format} instead.
     */
  var format: js.Any = js.native
  /**
     * @deprecated Use { Texture#type texture.type} instead.
     */
  var `type`: js.Any = js.native
  /**
     * @deprecated Use { Texture#generateMipmaps texture.generateMipmaps} instead.
     */
  var generateMipmaps: js.Any = js.native
  def setSize(width: Double, height: Double): Unit = js.native
  def copy(source: WebGLRenderTarget): js.Dynamic = js.native
  def dispose(): Unit = js.native
}