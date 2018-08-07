package minithree3

import scala.scalajs.js


@js.native
@js.annotation.JSGlobal("Vector3")
class Vector3 protected () extends Vector {
  def this(x: Double = js.native, y: Double = js.native, z: Double = js.native) = this()
  var x: Double = js.native
  var y: Double = js.native
  var z: Double = js.native
  /**
     * Sets value of this vector.
     */
  def set(x: Double, y: Double, z: Double): Vector3 = js.native
  /**
     * Sets all values of this vector.
     */
  def setScalar(scalar: Double): Vector3 = js.native
  /**
     * Sets x value of this vector.
     */
  def setX(x: Double): Vector3 = js.native
  /**
     * Sets y value of this vector.
     */
  def setY(y: Double): Vector3 = js.native
  /**
     * Sets z value of this vector.
     */
  def setZ(z: Double): Vector3 = js.native
  //def setComponent(index: Double, value: Double): Unit = js.native
  //def getComponent(index: Double): Double = js.native
  /**
     * Clones this vector.
     */
  /**
     * Copies value of v to this vector.
     */
  def copy(v: Vector3): js.Dynamic = js.native
  /**
     * Adds v to this vector.
     */
  def add(a: Vector3): Vector3 = js.native
  def addScalar(s: Double): Vector3 = js.native
  def addScaledVector(v: Vector3, s: Double): Vector3 = js.native
  /**
     * Sets this vector to a + b.
     */
  def addVectors(a: Vector3, b: Vector3): Vector3 = js.native
  /**
     * Subtracts v from this vector.
     */
  def sub(a: Vector3): Vector3 = js.native
  def subScalar(s: Double): Vector3 = js.native
  /**
     * Sets this vector to a - b.
     */
  def subVectors(a: Vector3, b: Vector3): Vector3 = js.native
  def multiply(v: Vector3): Vector3 = js.native
  /**
     * Multiplies this vector by scalar s.
     */
  //def multiplyScalar(s: Double): Vector3 = js.native
  def multiplyVectors(a: Vector3, b: Vector3): Vector3 = js.native
  def applyEuler(euler: Euler): Vector3 = js.native
  def applyAxisAngle(axis: Vector3, angle: Double): Vector3 = js.native
  def applyMatrix3(m: Matrix3): Vector3 = js.native
  def applyMatrix4(m: Matrix4): Vector3 = js.native
  def applyQuaternion(q: Quaternion): Vector3 = js.native
  def project(camrea: Camera): Vector3 = js.native
  def unproject(camera: Camera): Vector3 = js.native
  def transformDirection(m: Matrix4): Vector3 = js.native
  def divide(v: Vector3): Vector3 = js.native
  /**
     * Divides this vector by scalar s.
     * Set vector to ( 0, 0, 0 ) if s == 0.
     */
  //def divideScalar(s: Double): Vector3 = js.native
  def min(v: Vector3): Vector3 = js.native
  def max(v: Vector3): Vector3 = js.native
  def clamp(min: Vector3, max: Vector3): Vector3 = js.native
  def clampScalar(min: Double, max: Double): Vector3 = js.native
  def clampLength(min: Double, max: Double): Vector3 = js.native
  def floor(): Vector3 = js.native
  def ceil(): Vector3 = js.native
  def round(): Vector3 = js.native
  def roundToZero(): Vector3 = js.native
  /**
     * Inverts this vector.
     */
  //def negate(): Vector3 = js.native
  /**
     * Computes dot product of this vector and v.
     */
  def dot(v: Vector3): Double = js.native
  /**
     * Computes squared length of this vector.
     */
  //def lengthSq(): Double = js.native
  /**
     * Computes length of this vector.
     */
  //def length(): Double = js.native
  /**
     * Computes Manhattan length of this vector.
     * http://en.wikipedia.org/wiki/Taxicab_geometry
     *
     * @deprecated Use { Vector3#manhattanLength .manhattanLength()} instead.
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
     * Computes the Manhattan length (distance) from this vector to the given vector v
     *
     * @param {Vector3} v
     *
     * @return {number}
     *
     * @see { http://en.wikipedia.org/wiki/Taxicab_geometry|Wikipedia: Taxicab Geometry}
     */
  def manhattanDistanceTo(v: Vector3): Double = js.native
  /**
     * Normalizes this vector.
     */
  //def normalize(): Vector3 = js.native
  /**
     * Normalizes this vector and multiplies it by l.
     */
  //def setLength(l: Double): Vector3 = js.native
  def lerp(v: Vector3, alpha: Double): Vector3 = js.native
  def lerpVectors(v1: Vector3, v2: Vector3, alpha: Double): Vector3 = js.native
  /**
     * Sets this vector to cross product of itself and v.
     */
  def cross(a: Vector3): Vector3 = js.native
  /**
     * Sets this vector to cross product of a and b.
     */
  def crossVectors(a: Vector3, b: Vector3): Vector3 = js.native
  def projectOnVector(v: Vector3): Vector3 = js.native
  def projectOnPlane(planeNormal: Vector3): Vector3 = js.native
  def reflect(vector: Vector3): Vector3 = js.native
  def angleTo(v: Vector3): Double = js.native
  /**
     * Computes distance of this vector to v.
     */
  def distanceTo(v: Vector3): Double = js.native
  /**
     * Computes squared distance of this vector to v.
     */
  def distanceToSquared(v: Vector3): Double = js.native
  /**
     * @deprecated Use { Vector3#manhattanDistanceTo .manhattanDistanceTo()} instead.
     */
  def distanceToManhattan(v: Vector3): Double = js.native
  def setFromSpherical(s: Spherical): Vector3 = js.native
  def setFromMatrixPosition(m: Matrix4): Vector3 = js.native
  def setFromMatrixScale(m: Matrix4): Vector3 = js.native
  def setFromMatrixColumn(matrix: Matrix4, index: Double): Vector3 = js.native
  /**
     * Checks for strict equality of this vector and v.
     */
  def equals(v: Vector3): Boolean = js.native
  def fromArray(xyz: js.Array[Double], offset: Double = js.native): Vector3 = js.native
  def toArray(xyz: js.Array[Double] = js.native, offset: Double = js.native): js.Array[Double] = js.native
  def fromBufferAttribute(attribute: BufferAttribute, index: Double, offset: Double = js.native): Vector3 = js.native
  /**
     * @deprecated Use { Vector3#setFromMatrixPosition .setFromMatrixPosition()} instead.
     */
  def getPositionFromMatrix(m: Matrix4): Vector3 = js.native
  /**
     * @deprecated Use { Vector3#setFromMatrixScale .setFromMatrixScale()} instead.
     */
  def getScaleFromMatrix(m: Matrix4): Vector3 = js.native
  /**
     * @deprecated Use { Vector3#setFromMatrixColumn .setFromMatrixColumn()} instead.
     */
  def getColumnFromMatrix(index: Double, matrix: Matrix4): Vector3 = js.native
}