package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLRenderTargetCube")
class WebGLRenderTargetCube protected () extends WebGLRenderTarget {
  def this(width: Double, height: Double, options: WebGLRenderTargetOptions = js.native) = this()
  var activeCubeFace: Double = js.native
  // PX 0, NX 1, PY 2, NY 3, PZ 4, NZ 5
  var activeMipMapLevel: Double = js.native
}