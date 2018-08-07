package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Cylindrical")
class Cylindrical protected () extends js.Object {
  def this(radius: Double = js.native, theta: Double = js.native, y: Double = js.native) = this()
  var radius: Double = js.native
  var theta: Double = js.native
  var y: Double = js.native
  def copy(other: Cylindrical): js.Dynamic = js.native
  def set(radius: Double, theta: Double, y: Double): js.Dynamic = js.native
  def setFromVector3(vec3: Vector3): js.Dynamic = js.native
}