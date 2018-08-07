package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait WebVRManager extends js.Object {
  var enabled: Boolean = js.native
  def getDevice(): js.Object | Null = js.native
  def setDevice(device: js.Object | Null): Unit = js.native
  def setPoseTarget(`object`: Object3D | Null): Unit = js.native
  def getCamera(camera: PerspectiveCamera): PerspectiveCamera | ArrayCamera = js.native
  def submitFrame(): Unit = js.native
  def dispose(): Unit = js.native
}