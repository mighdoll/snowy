package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ShapeGeometry")
class ShapeGeometry protected () extends Geometry {
  def this(shape: Shape, options: js.Any) = this()
  def this(shapes: js.Array[Shape], options: js.Any) = this()
  def addShapeList(shapes: js.Array[Shape], options: js.Any): ShapeGeometry = js.native
  def addShape(shape: Shape, options: js.Any = js.native): Unit = js.native
}