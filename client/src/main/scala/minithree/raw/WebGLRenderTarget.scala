package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait WebGLRenderTargetOptions extends js.Object {
  var wrapS: Wrapping          = js.native
  var wrapT: Wrapping          = js.native
  var magFilter: TextureFilter = js.native
  var minFilter: TextureFilter = js.native
  var anisotropy: Double       = js.native
  var format: Double           = js.native
  var `type`: TextureDataType  = js.native
  var depthBuffer: Boolean     = js.native
  var stencilBuffer: Boolean   = js.native
}

@js.native
@JSName("THREE.WebGLRenderTarget")
class WebGLRenderTarget(var width: Double,
                        var height: Double,
                        options: WebGLRenderTargetOptions = js.native)
    extends RenderTarget with EventDispatcher {
  var wrapS: Wrapping                     = js.native
  var wrapT: Wrapping                     = js.native
  var magFilter: TextureFilter            = js.native
  var minFilter: TextureFilter            = js.native
  var anisotropy: Double                  = js.native
  var offset: Vector2                     = js.native
  var repeat: Vector2                     = js.native
  var format: Double                      = js.native
  var `type`: Double                      = js.native
  var depthBuffer: Boolean                = js.native
  var stencilBuffer: Boolean              = js.native
  var generateMipmaps: Boolean            = js.native
  var shareDepthFrom: js.Any              = js.native
  override def clone(): WebGLRenderTarget = js.native
  def dispose(): Unit                     = js.native
}

@js.native
trait WebGLRenderingContext extends js.Object
