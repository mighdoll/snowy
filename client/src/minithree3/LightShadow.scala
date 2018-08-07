package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LightShadow")
class LightShadow protected () extends js.Object {
  def this(camera: Camera) = this()
  var camera: Camera = js.native
  var bias: Double = js.native
  var radius: Double = js.native
  var mapSize: Vector2 = js.native
  var map: RenderTarget = js.native
  var matrix: Matrix4 = js.native
  def copy(source: LightShadow): LightShadow = js.native
  def toJSON(): js.Dynamic = js.native
}