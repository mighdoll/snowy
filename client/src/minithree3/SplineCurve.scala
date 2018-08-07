package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("SplineCurve")
class SplineCurve protected () extends Curve[Vector2] {
  def this(points: js.Array[Vector2] = js.native) = this()
  var points: js.Array[Vector2] = js.native
}