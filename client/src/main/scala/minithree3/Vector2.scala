package minithree3

import scala.scalajs.js


  /**
     * clone():T;
     */
@js.native
@js.annotation.JSGlobal("Vector2")
class Vector2 protected () extends Vector {
  def this(x: Double = js.native, y: Double = js.native) = this()
  var x: Double = js.native
  var y: Double = js.native
  var width: Double = js.native
  var height: Double = js.native
  /**
     * Sets value of this vector.
     */
  def set(x: Double, y: Double): Vector2 = js.native
  /**
     * Sets the x and y values of this vector both equal to scalar.
     */
  def setScalar(scalar: Double): Vector2 = js.native
  /**
     * Sets X component of this vector.
     */
  def setX(x: Double): Vector2 = js.native
  /**
     * Sets Y component of this vector.
     */
  def setY(y: Double): Vector2 = js.native
  /**
     * Sets a component of this vector.
     */
  //def setComponent(index: Double, value: Double): Unit = js.native
  /**
     * Gets a component of this vector.
     */
  //def getComponent(index: Double): Double = js.native
  /**
     * Clones this vector.
     */
  /**
     * Copies value of v to this vector.
     */
  def copy(v: Vector2): js.Dynamic = js.native
  /**
     * Adds v to this vector.
     */
  def add(v: Vector2): Vector2 = js.native
  /**
     * Adds the scalar value s to this vector's x and y values.
     */
  def addScalar(s: Double): Vector2 = js.native
  /**
     * Sets this vector to a + b.
     */
  def addVectors(a: Vector2, b: Vector2): Vector2 = js.native
  /**
     * Adds the multiple of v and s to this vector.
     */
  def addScaledVector(v: Vector2, s: Double): Vector2 = js.native
  /**
     * Subtracts v from this vector.
    */
  def sub(v: Vector2): Vector2 = js.native
  /**
     * Subtracts s from this vector's x and y components.
     */
  def subScalar(s: Double): Vector2 = js.native
  /**
     * Sets this vector to a - b.
     */
  def subVectors(a: Vector2, b: Vector2): Vector2 = js.native
  /**
     * Multiplies this vector by v.
     */
  def multiply(v: Vector2): Vector2 = js.native
  /**
     * Multiplies this vector by scalar s.
     */
  //def multiplyScalar(scalar: Double): Vector2 = js.native
  /**
     * Divides this vector by v.
     */
  def divide(v: Vector2): Vector2 = js.native
  /**
     * Divides this vector by scalar s.
     * Set vector to ( 0, 0 ) if s == 0.
     */
  //def divideScalar(s: Double): Vector2 = js.native
  /**
     * Multiplies this vector (with an implicit 1 as the 3rd component) by m.
     */
  def applyMatrix3(m: Matrix3): Vector2 = js.native
  /**
     * If this vector's x or y value is greater than v's x or y value, replace that value with the corresponding min value.
     */
  def min(v: Vector2): Vector2 = js.native
  /**
     * If this vector's x or y value is less than v's x or y value, replace that value with the corresponding max value.
     */
  def max(v: Vector2): Vector2 = js.native
  /**
     * If this vector's x or y value is greater than the max vector's x or y value, it is replaced by the corresponding value.
     * If this vector's x or y value is less than the min vector's x or y value, it is replaced by the corresponding value.
     * @param min the minimum x and y values.
     * @param max the maximum x and y values in the desired range.
     */
  def clamp(min: Vector2, max: Vector2): Vector2 = js.native
  /**
     * If this vector's x or y values are greater than the max value, they are replaced by the max value.
     * If this vector's x or y values are less than the min value, they are replaced by the min value.
     * @param min the minimum value the components will be clamped to.
     * @param max the maximum value the components will be clamped to.
     */
  def clampScalar(min: Double, max: Double): Vector2 = js.native
  /**
     * If this vector's length is greater than the max value, it is replaced by the max value.
     * If this vector's length is less than the min value, it is replaced by the min value.
     * @param min the minimum value the length will be clamped to.
     * @param max the maximum value the length will be clamped to.
     */
  def clampLength(min: Double, max: Double): Vector2 = js.native
  /**
     * The components of the vector are rounded down to the nearest integer value.
     */
  def floor(): Vector2 = js.native
  /**
     * The x and y components of the vector are rounded up to the nearest integer value.
     */
  def ceil(): Vector2 = js.native
  /**
     * The components of the vector are rounded to the nearest integer value.
     */
  def round(): Vector2 = js.native
  /**
     * The components of the vector are rounded towards zero (up if negative, down if positive) to an integer value.
     */
  def roundToZero(): Vector2 = js.native
  /**
     * Inverts this vector.
     */
  //def negate(): Vector2 = js.native
  /**
     * Computes dot product of this vector and v.
     */
  def dot(v: Vector2): Double = js.native
  /**
     * Computes squared length of this vector.
     */
  //def lengthSq(): Double = js.native
  /**
     * Computes length of this vector.
     */
  //def length(): Double = js.native
  /**
     * @deprecated Use { Vector2#manhattanLength .manhattanLength()} instead.
     */
  def lengthManhattan(): Double = js.native
  /**
     * Normalizes this vector.
     */
  //def normalize(): Vector2 = js.native
  /**
     * computes the angle in radians with respect to the positive x-axis
     */
  def angle(): Double = js.native
  /**
     * Computes distance of this vector to v.
     */
  def distanceTo(v: Vector2): Double = js.native
  /**
     * Computes squared distance of this vector to v.
     */
  def distanceToSquared(v: Vector2): Double = js.native
  /**
     * @deprecated Use { Vector2#manhattanDistanceTo .manhattanDistanceTo()} instead.
     */
  def distanceToManhattan(v: Vector2): Double = js.native
  /**
     * Normalizes this vector and multiplies it by l.
     */
  //def setLength(length: Double): Vector2 = js.native
  /**
     * Linearly interpolates between this vector and v, where alpha is the distance along the line - alpha = 0 will be this vector, and alpha = 1 will be v.
     * @param v vector to interpolate towards.
     * @param alpha interpolation factor in the closed interval [0, 1].
     */
  def lerp(v: Vector2, alpha: Double): Vector2 = js.native
  /**
     * Sets this vector to be the vector linearly interpolated between v1 and v2 where alpha is the distance along the line connecting the two vectors - alpha = 0 will be v1, and alpha = 1 will be v2.
     * @param v1 the starting vector.
     * @param v2 vector to interpolate towards.
     * @param alpha interpolation factor in the closed interval [0, 1].
     */
  def lerpVectors(v1: Vector2, v2: Vector2, alpha: Double): Vector2 = js.native
  /**
     * Checks for strict equality of this vector and v.
     */
  def equals(v: Vector2): Boolean = js.native
  /**
     * Sets this vector's x value to be array[offset] and y value to be array[offset + 1].
     * @param array the source array.
     * @param offset (optional) offset into the array. Default is 0.
     */
  def fromArray(array: js.Array[Double], offset: Double = js.native): Vector2 = js.native
  /**
     * Returns an array [x, y], or copies x and y into the provided array.
     * @param array (optional) array to store the vector to. If this is not provided, a new array will be created.
     * @param offset (optional) optional offset into the array.
     */
  def toArray(array: js.Array[Double] = js.native, offset: Double = js.native): js.Array[Double] = js.native
  /**
     * Sets this vector's x and y values from the attribute.
     * @param attribute the source attribute.
     * @param index index in the attribute.
     */
  def fromBufferAttribute(attribute: BufferAttribute, index: Double): Vector2 = js.native
  /**
     * Rotates the vector around center by angle radians.
     * @param center the point around which to rotate.
     * @param angle the angle to rotate, in radians.
     */
  def rotateAround(center: Vector2, angle: Double): Vector2 = js.native
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
     * @param {Vector2} v
     *
     * @return {number}
     *
     * @see { http://en.wikipedia.org/wiki/Taxicab_geometry|Wikipedia: Taxicab Geometry}
     */
  def manhattanDistanceTo(v: Vector2): Double = js.native
}