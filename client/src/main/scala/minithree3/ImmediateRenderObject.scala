package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ImmediateRenderObject")
class ImmediateRenderObject protected () extends Object3D {
  def this(material: Material) = this()
  var material: Material = js.native
  def render(renderCallback: js.Function): Unit = js.native
}