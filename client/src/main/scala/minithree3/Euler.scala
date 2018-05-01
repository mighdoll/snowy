package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Euler")
class Euler protected () extends js.Object {
  def this(x: Double = js.native, y: Double = js.native, z: Double = js.native, order: String = js.native) = this()
  var x: Double = js.native
  var y: Double = js.native
  var z: Double = js.native
  var order: String = js.native
  var onChangeCallback: js.Function = js.native
  def set(x: Double, y: Double, z: Double, order: String = js.native): Euler = js.native
  def copy(euler: Euler): js.Dynamic = js.native
  def setFromRotationMatrix(m: Matrix4, order: String = js.native, update: Boolean = js.native): Euler = js.native
  def setFromQuaternion(q: Quaternion, order: String = js.native, update: Boolean = js.native): Euler = js.native
  def setFromVector3(v: Vector3, order: String = js.native): Euler = js.native
  def reorder(newOrder: String): Euler = js.native
  def equals(euler: Euler): Boolean = js.native
  def fromArray(xyzo: js.Array[js.Any]): Euler = js.native
  def toArray(array: js.Array[Double] = js.native, offset: Double = js.native): js.Array[Double] = js.native
  def toVector3(optionalResult: Vector3 = js.native): Vector3 = js.native
  def onChange(callback: js.Function): Unit = js.native
}

@js.native
@js.annotation.JSGlobal("Euler")
object Euler extends js.Object {
  var RotationOrders: js.Array[String] = js.native
  var DefaultOrder: String = js.native
}