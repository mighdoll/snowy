package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLColorBuffer")
class WebGLColorBuffer protected () extends js.Object {
  def this(gl: js.Any, state: js.Any) = this()
  def setMask(colorMask: Double): Unit = js.native
  def setLocked(lock: Boolean): Unit = js.native
  def setClear(r: Double, g: Double, b: Double, a: Double): Unit = js.native
  def reset(): Unit = js.native
}