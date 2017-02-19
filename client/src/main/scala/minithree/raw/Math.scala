package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Spherical")
class Spherical(var radius: Double = js.native,
                var phi: Double = js.native,
                var theta: Double = js.native)
    extends js.Object {
  def set(radius: Double, phi: Double, theta: Double): Spherical = js.native
  override def clone(): Spherical                                = js.native
  def copy(other: Spherical): Spherical                          = js.native
  // restrict phi to be betwee EPS and PI-EPS
  def makeSafe(): Spherical                    = js.native
  def setFromVector3(vec3: Vector3): Spherical = js.native
}

@js.native
@JSName("THREE.Euler")
class Euler(var x: Double = js.native,
            var y: Double = js.native,
            var z: Double = js.native,
            var order: String = js.native)
    extends js.Object {
  var onChange: js.Function0[Unit] = js.native

  def set(x: Double, y: Double, z: Double, order: String = js.native): Euler = js.native

  def copy(euler: Euler): Euler = js.native

  def setFromRotationMatrix(m: Matrix4, order: String = js.native): Euler = js.native

  def setFromQuaternion(q: Quaternion,
                        order: String = js.native,
                        update: Boolean = js.native): Euler = js.native

  def reorder(newOrder: String): Euler = js.native

  def equals(euler: Euler): Boolean = js.native

  def fromArray(xyzo: js.Array[js.Any]): Euler = js.native

  def toArray(): js.Array[js.Any] = js.native

  override def clone(): Euler = js.native
}

@js.native
@JSName("THREE.Quaternion")
class Quaternion(var x: Double = js.native,
                 var y: Double = js.native,
                 var z: Double = js.native,
                 var w: Double = js.native)
    extends js.Object {
  var onChange: js.Function0[Unit] = js.native

  def set(x: Double, y: Double, z: Double, w: Double): Quaternion = js.native

  def copy(q: Quaternion): Quaternion = js.native

  def setFromEuler(euler: Euler, update: Boolean = js.native): Quaternion = js.native

  def setFromAxisAngle(axis: Vector3, angle: Double): Quaternion = js.native

  def setFromRotationMatrix(m: Matrix4): Quaternion = js.native

  def setFromUnitVectors(vFrom: Vector3, vTo: Vector3): Quaternion = js.native

  def inverse(): Quaternion = js.native

  def conjugate(): Quaternion = js.native

  def dot(v: Quaternion): Double = js.native

  def lengthSq(): Double = js.native

  def length(): Double = js.native

  def normalize(): Quaternion = js.native

  def multiply(q: Quaternion): Quaternion = js.native

  def multiplyQuaternions(a: Quaternion, b: Quaternion): Quaternion = js.native

  def multiplyVector3(vector: Vector3): Vector3 = js.native

  def slerp(qb: Quaternion, t: Double): Quaternion = js.native

  def equals(v: Quaternion): Boolean = js.native

  def fromArray(n: js.Array[Double]): Quaternion = js.native

  def toArray(): js.Array[Double] = js.native

  override def clone(): Quaternion = js.native
}
