package minithree3

import scala.scalajs.js
import com.definitelyscala.node.Buffer

@js.native
@js.annotation.JSGlobal("Vector4")
class Vector4 protected () extends Vector {
  def this(x: Double = js.native, y: Double = js.native, z: Double = js.native, w: Double = js.native) = this()
  var x: Double = js.native
  var y: Double = js.native
  var z: Double = js.native
  var w: Double = js.native
  /**
     * Sets value of this vector.
     */
  def set(x: Double, y: Double, z: Double, w: Double): Vector4 = js.native
  /**
     * Sets all values of this vector.
     */
  def setScalar(scalar: Double): Vector4 = js.native
  /**
     * Sets X component of this vector.
     */
  def setX(x: Double): Vector4 = js.native
  /**
     * Sets Y component of this vector.
     */
  def setY(y: Double): Vector4 = js.native
  /**
     * Sets Z component of this vector.
     */
  def setZ(z: Double): Vector4 = js.native
  /**
     * Sets w component of this vector.
     */
  def setW(w: Double): Vector4 = js.native
  def setComponent(index: Double, value: Double): Unit = js.native
  def getComponent(index: Double): Double = js.native
  /**
     * Clones this vector.
     */
  /**
     * Copies value of v to this vector.
     */
  def copy(v: Vector4): js.Dynamic = js.native
  /**
     * Adds v to this vector.
     */
  def add(v: Vector4): Vector4 = js.native
  def addScalar(s: Double): Vector4 = js.native
  /**
     * Sets this vector to a + b.
     */
  def addVectors(a: Vector4, b: Vector4): Vector4 = js.native
  def addScaledVector(v: Vector4, s: Double): Vector4 = js.native
  /**
     * Subtracts v from this vector.
     */
  def sub(v: Vector4): Vector4 = js.native
  def subScalar(s: Double): Vector4 = js.native
  /**
     * Sets this vector to a - b.
     */
  def subVectors(a: Vector4, b: Vector4): Vector4 = js.native
  /**
     * Multiplies this vector by scalar s.
     */
  def multiplyScalar(s: Double): Vector4 = js.native
  def applyMatrix4(m: Matrix4): Vector4 = js.native
  /**
     * Divides this vector by scalar s.
     * Set vector to ( 0, 0, 0 ) if s == 0.
     */
  def divideScalar(s: Double): Vector4 = js.native
  /**
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToAngle/index.htm
     * @param q is assumed to be normalized
     */
  def setAxisAngleFromQuaternion(q: Quaternion): Vector4 = js.native
  /**
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToAngle/index.htm
     * @param m assumes the upper 3x3 of m is a pure rotation matrix (i.e, unscaled)
     */
  def setAxisAngleFromRotationMatrix(m: Matrix3): Vector4 = js.native
  def min(v: Vector4): Vector4 = js.native
  def max(v: Vector4): Vector4 = js.native
  def clamp(min: Vector4, max: Vector4): Vector4 = js.native
  def clampScalar(min: Double, max: Double): Vector4 = js.native
  def floor(): Vector4 = js.native
  def ceil(): Vector4 = js.native
  def round(): Vector4 = js.native
  def roundToZero(): Vector4 = js.native
  /**
     * Inverts this vector.
     */
  def negate(): Vector4 = js.native
  /**
     * Computes dot product of this vector and v.
     */
  def dot(v: Vector4): Double = js.native
  /**
     * Computes squared length of this vector.
     */
  def lengthSq(): Double = js.native
  /**
     * Computes length of this vector.
     */
  def length(): Double = js.native
  /**
     * @deprecated Use { Vector4#manhattanLength .manhattanLength()} instead.
     */
  def lengthManhattan(): Double = js.native
  /**
     * Computes the Manhattan length of this vector.
     *
     * @return {number}
     *
     * @see { http://en.wikipedia.org/wiki/Taxicab_geometry|Wikipedia: Taxicab Geometry}
     */
  def manhattanLength(): Double = js.native
  /**
     * Normalizes this vector.
     */
  def normalize(): Vector4 = js.native
  /**
     * Normalizes this vector and multiplies it by l.
     */
  def setLength(length: Double): Vector4 = js.native
  /**
     * Linearly interpolate between this vector and v with alpha factor.
     */
  def lerp(v: Vector4, alpha: Double): Vector4 = js.native
  def lerpVectors(v1: Vector4, v2: Vector4, alpha: Double): Vector4 = js.native
  /**
     * Checks for strict equality of this vector and v.
     */
  def equals(v: Vector4): Boolean = js.native
  def fromArray(xyzw: js.Array[Double], offset: Double = js.native): Vector4 = js.native
  def toArray(xyzw: js.Array[Double] = js.native, offset: Double = js.native): js.Array[Double] = js.native
  def fromBufferAttribute(attribute: BufferAttribute, index: Double, offset: Double = js.native): Vector4 = js.native
}