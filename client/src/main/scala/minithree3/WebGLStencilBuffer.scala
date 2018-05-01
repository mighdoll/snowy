package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLStencilBuffer")
class WebGLStencilBuffer protected () extends js.Object {
  def this(gl: js.Any, state: js.Any) = this()
  def setTest(stencilTest: Boolean): Unit = js.native
  def sertMask(stencilMask: Double): Unit = js.native
  def setFunc(stencilFunc: js.Function, stencilRef: js.Any, stencilMask: Double): Unit = js.native
  def setOp(stencilFail: js.Any, stencilZFail: js.Any, stencilZPass: js.Any): Unit = js.native
  def setLocked(lock: Boolean): Unit = js.native
  def setClear(stencil: js.Any): Unit = js.native
  def reset(): Unit = js.native
}