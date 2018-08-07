package minithree3

import scala.scalajs.js

@js.native
trait WebGLRenderTargetOptions extends js.Object {
  var wrapS: Wrapping = js.native
  var wrapT: Wrapping = js.native
  var magFilter: TextureFilter = js.native
  var minFilter: TextureFilter = js.native
  var format: Double = js.native
  // RGBAFormat;
  var `type`: TextureDataType = js.native
  // UnsignedByteType;
  var anisotropy: Double = js.native
  // 1;
  var depthBuffer: Boolean = js.native
  // true;
  var stencilBuffer: Boolean = js.native
}