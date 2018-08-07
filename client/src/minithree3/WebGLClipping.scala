package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLClipping")
class WebGLClipping extends js.Object {
  var uniform: js.Any = js.native
  var numPlanes: Double = js.native
  def init(planes: js.Array[js.Any], enableLocalClipping: Boolean, camera: Camera): Boolean = js.native
  def beginShadows(): Unit = js.native
  def endShadows(): Unit = js.native
  def setState(planes: js.Array[js.Any], clipShadows: Boolean, camera: Camera, cache: Boolean, fromCache: Boolean): Unit = js.native
}