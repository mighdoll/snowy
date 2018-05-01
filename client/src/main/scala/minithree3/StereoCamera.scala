package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("StereoCamera")
class StereoCamera extends Camera {
  var aspect: Double = js.native
  var eyeSep: Double = js.native
  var cameraL: PerspectiveCamera = js.native
  var cameraR: PerspectiveCamera = js.native
  def update(camera: PerspectiveCamera): Unit = js.native
}