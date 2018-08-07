package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("HemisphereLightHelper")
class HemisphereLightHelper protected () extends Object3D {
  def this(light: Light, sphereSize: Double) = this()
  var light: Light = js.native
  var colors: js.Array[Color] = js.native
  var lightSphere: Mesh = js.native
  def dispose(): Unit = js.native
  def update(): Unit = js.native
}