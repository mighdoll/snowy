package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ExtrudeGeometry")
class ExtrudeGeometry protected () extends Geometry {
  def this(shape: Shape = js.native, options: js.Any = js.native) = this()
  def this(shapes: js.Array[Shape] = js.native, options: js.Any = js.native) = this()
  def addShapeList(shapes: js.Array[Shape], options: js.Any = js.native): Unit = js.native
  def addShape(shape: Shape, options: js.Any = js.native): Unit = js.native
}

@js.native
@js.annotation.JSGlobal("ExtrudeGeometry")
object ExtrudeGeometry extends js.Object {
  var WorldUVGenerator: js.Any = js.native
}