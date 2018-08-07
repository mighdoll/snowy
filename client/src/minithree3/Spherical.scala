package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Spherical")
class Spherical protected () extends js.Object {
  def this(radius: Double = js.native, phi: Double = js.native, theta: Double = js.native) = this()
  var radius: Double = js.native
  var phi: Double = js.native
  var theta: Double = js.native
  def set(radius: Double, phi: Double, theta: Double): Spherical = js.native
  def copy(other: Spherical): js.Dynamic = js.native
  def makeSafe(): Unit = js.native
  def setFromVector3(vec3: Vector3): Spherical = js.native
}