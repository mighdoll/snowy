package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
trait Renderer extends js.Object {
  var domElement: HTMLCanvasElement = js.native
  def render(scene: Scene, camera: Camera): Unit = js.native
  def setSize(width: Double, height: Double, updateStyle: Boolean = js.native): Unit = js.native
}