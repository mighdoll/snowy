package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLDepthBuffer")
class WebGLDepthBuffer protected () extends js.Object {
  def this(gl: js.Any, state: js.Any) = this()
  def setTest(depthTest: Boolean): Unit = js.native
  def sertMask(depthMask: Double): Unit = js.native
  def setFunc(depthFunc: js.Function): Unit = js.native
  def setLocked(lock: Boolean): Unit = js.native
  def setClear(depth: js.Any): Unit = js.native
  def reset(): Unit = js.native
}