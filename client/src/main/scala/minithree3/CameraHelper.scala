package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CameraHelper")
class CameraHelper protected () extends LineSegments {
  def this(camera: Camera) = this()
  var camera: Camera = js.native
  var pointMap: js.Dictionary[js.Array[Double]] = js.native
  def update(): Unit = js.native
}