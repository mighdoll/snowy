package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("PlaneHelper")
class PlaneHelper protected () extends LineSegments {
  def this(plane: Plane, size: Double = js.native, hex: Double = js.native) = this()
  var plane: Plane = js.native
  var size: Double = js.native
  def updateMatrixWorld(force: Boolean): Unit = js.native
}