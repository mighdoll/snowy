package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Layers")
class Layers extends js.Object {
  var mask: Double = js.native
  def set(channel: Double): Unit = js.native
  def enable(channel: Double): Unit = js.native
  def toggle(channel: Double): Unit = js.native
  def disable(channel: Double): Unit = js.native
  def test(layers: Layers): Boolean = js.native
}