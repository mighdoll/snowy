package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ExtrudeGeometry")
class ExtrudeGeometry protected () extends Geometry {
  def this(shape: Shape, options: js.Any) = this()
  def this(shapes: js.Array[Shape], options: js.Any) = this()
  def addShapeList(shapes: js.Array[Shape], options: js.Any): Unit = js.native
  def addShape(shape: Shape, options: js.Any): Unit = js.native
}

@js.native
@js.annotation.JSGlobal("ExtrudeGeometry")
object ExtrudeGeometry extends js.Object {
  var WorldUVGenerator: js.Any = js.native
}