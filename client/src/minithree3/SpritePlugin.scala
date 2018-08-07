package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("SpritePlugin")
class SpritePlugin protected () extends js.Object {
  def this(renderer: WebGLRenderer, sprites: js.Array[js.Any]) = this()
  def render(scene: Scene, camera: Camera, viewportWidth: Double, viewportHeight: Double): Unit = js.native
}