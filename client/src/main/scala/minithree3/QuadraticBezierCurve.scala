package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("QuadraticBezierCurve")
class QuadraticBezierCurve protected () extends Curve[Vector2] {
  def this(v0: Vector2, v1: Vector2, v2: Vector2) = this()
  var v0: Vector2 = js.native
  var v1: Vector2 = js.native
  var v2: Vector2 = js.native
}