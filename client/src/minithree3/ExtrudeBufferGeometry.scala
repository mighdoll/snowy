package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ExtrudeBufferGeometry")
class ExtrudeBufferGeometry protected () extends BufferGeometry {
  def this(shapes: js.Array[Shape] = js.native, options: js.Any = js.native) = this()
  def addShapeList(shapes: js.Array[Shape], options: js.Any = js.native): Unit = js.native
  def addShape(shape: Shape, options: js.Any = js.native): Unit = js.native
}

@js.native
@js.annotation.JSGlobal("ExtrudeBufferGeometry")
object ExtrudeBufferGeometry extends js.Object {
  var WorldUVGenerator: js.Any = js.native
}