package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LineCurve")
class LineCurve protected () extends Curve[Vector2] {
  def this(v1: Vector2, v2: Vector2) = this()
  var v1: Vector2 = js.native
  var v2: Vector2 = js.native
}