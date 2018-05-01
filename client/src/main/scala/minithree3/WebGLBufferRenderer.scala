package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
@js.annotation.JSGlobal("WebGLBufferRenderer")
class WebGLBufferRenderer protected () extends js.Object {
  def this(_gl: WebGLRenderingContext, extensions: js.Any, _infoRender: js.Any) = this()
  def setMode(value: js.Any): Unit = js.native
  def render(start: js.Any, count: Double): Unit = js.native
  def renderInstances(geometry: js.Any): Unit = js.native
}