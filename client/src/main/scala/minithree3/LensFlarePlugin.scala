package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LensFlarePlugin")
class LensFlarePlugin protected () extends js.Object {
  def this(renderer: WebGLRenderer, flares: js.Array[js.Any]) = this()
  def render(scene: Scene, camera: Camera, viewportWidth: Double, viewportHeight: Double): Unit = js.native
}