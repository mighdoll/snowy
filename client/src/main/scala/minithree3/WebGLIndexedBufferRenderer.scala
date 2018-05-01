package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
@js.annotation.JSGlobal("WebGLIndexedBufferRenderer")
class WebGLIndexedBufferRenderer protected () extends js.Object {
  def this(gl: WebGLRenderingContext, properties: js.Any, info: js.Any) = this()
  def setMode(value: js.Any): Unit = js.native
  def setIndex(index: js.Any): Unit = js.native
  def render(start: js.Any, count: Double): Unit = js.native
  def renderInstances(geometry: js.Any, start: js.Any, count: Double): Unit = js.native
}