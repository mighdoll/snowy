package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("BoundingBoxHelper")
class BoundingBoxHelper protected () extends Mesh {
  def this(`object`: Object3D = js.native, hex: Double = js.native) = this()
  var `object`: Object3D = js.native
  var box: Box3 = js.native
  def update(): Unit = js.native
}