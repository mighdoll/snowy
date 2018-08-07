package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Quaternion")
class Quaternion protected () extends js.Object {
  def this(x: Double = js.native, y: Double = js.native, z: Double = js.native, w: Double = js.native) = this()
  /**
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param w w coordinate
     */
  var x: Double = js.native
  var y: Double = js.native
  var z: Double = js.native
  var w: Double = js.native
  /**
     * Sets values of this quaternion.
     */
  def set(x: Double, y: Double, z: Double, w: Double): Quaternion = js.native
  /**
     * Clones this quaternion.
     */
  /**
     * Copies values of q to this quaternion.
     */
  def copy(q: Quaternion): js.Dynamic = js.native
  /**
     * Sets this quaternion from rotation specified by Euler angles.
     */
  def setFromEuler(euler: Euler, update: Boolean = js.native): Quaternion = js.native
  /**
     * Sets this quaternion from rotation specified by axis and angle.
     * Adapted from http://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToQuaternion/index.htm.
     * Axis have to be normalized, angle is in radians.
     */
  def setFromAxisAngle(axis: Vector3, angle: Double): Quaternion = js.native
  /**
     * Sets this quaternion from rotation component of m. Adapted from http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm.
     */
  def setFromRotationMatrix(m: Matrix4): Quaternion = js.native
  def setFromUnitVectors(vFrom: Vector3, vTo: Vector3): Quaternion = js.native
  /**
     * Inverts this quaternion.
     */
  def inverse(): Quaternion = js.native
  def conjugate(): Quaternion = js.native
  def dot(v: Quaternion): Double = js.native
  def lengthSq(): Double = js.native
  /**
     * Computes length of this quaternion.
     */
  def length(): Double = js.native
  /**
     * Normalizes this quaternion.
     */
  def normalize(): Quaternion = js.native
  /**
     * Multiplies this quaternion by b.
     */
  def multiply(q: Quaternion): Quaternion = js.native
  def premultiply(q: Quaternion): Quaternion = js.native
  /**
     * Sets this quaternion to a x b
     * Adapted from http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/code/index.htm.
     */
  def multiplyQuaternions(a: Quaternion, b: Quaternion): Quaternion = js.native
  def slerp(qb: Quaternion, t: Double): Quaternion = js.native
  def equals(v: Quaternion): Boolean = js.native
  def fromArray(n: js.Array[Double]): Quaternion = js.native
  def toArray(): js.Array[Double] = js.native
  def fromArray(xyzw: js.Array[Double], offset: Double): Quaternion = js.native
  def toArray(xyzw: js.Array[Double], offset: Double): js.Array[Double] = js.native
  def onChange(callback: js.Function): Quaternion = js.native
  var onChangeCallback: js.Function = js.native
  /**
     * Adapted from http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/slerp/.
     */
  /**
     * @deprecated Use { Vector#applyQuaternion vector.applyQuaternion( quaternion )} instead.
     */
  def multiplyVector3(v: js.Any): js.Dynamic = js.native
}

@js.native
@js.annotation.JSGlobal("Quaternion")
object Quaternion extends js.Object {
  def slerp(qa: Quaternion, qb: Quaternion, qm: Quaternion, t: Double): Quaternion = js.native
  def slerpFlat(dst: js.Array[Double], dstOffset: Double, src0: js.Array[Double], srcOffset: Double, src1: js.Array[Double], stcOffset1: Double, t: Double): Quaternion = js.native
}