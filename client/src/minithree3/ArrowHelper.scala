package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ArrowHelper")
class ArrowHelper protected () extends Object3D {
  def this(dir: Vector3, origin: Vector3 = js.native, length: Double = js.native, hex: Double = js.native, headLength: Double = js.native, headWidth: Double = js.native) = this()
  var line: Line = js.native
  var cone: Mesh = js.native
  def setDirection(dir: Vector3): Unit = js.native
  def setLength(length: Double, headLength: Double = js.native, headWidth: Double = js.native): Unit = js.native
  def setColor(hex: Double): Unit = js.native
}