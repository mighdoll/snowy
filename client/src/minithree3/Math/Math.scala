package minithree3.Math

import scala.scalajs.js
@js.native
@js.annotation.JSGlobal("Math")
object Math extends js.Object {
  val DEG2RAD: Double = js.native
  val RAD2DEG: Double = js.native
  def generateUUID(): String = js.native
  /**
     * Clamps the x to be between a and b.
     *
     * @param value Value to be clamped.
     * @param min Minimum value
     * @param max Maximum value.
     */
  def clamp(value: Double, min: Double, max: Double): Double = js.native
  def euclideanModulo(n: Double, m: Double): Double = js.native
  /**
     * Linear mapping of x from range [a1, a2] to range [b1, b2].
     *
     * @param x Value to be mapped.
     * @param a1 Minimum value for range A.
     * @param a2 Maximum value for range A.
     * @param b1 Minimum value for range B.
     * @param b2 Maximum value for range B.
     */
  def mapLinear(x: Double, a1: Double, a2: Double, b1: Double, b2: Double): Double = js.native
  def smoothstep(x: Double, min: Double, max: Double): Double = js.native
  def smootherstep(x: Double, min: Double, max: Double): Double = js.native
  /**
     * Random float from 0 to 1 with 16 bits of randomness.
     * Standard Math.random() creates repetitive patterns when applied over larger space.
     *
     * @deprecated Use { Math#random Math.random()}
     */
  def random16(): Double = js.native
  /**
     * Random integer from low to high interval.
     */
  def randInt(low: Double, high: Double): Double = js.native
  /**
     * Random float from low to high interval.
     */
  def randFloat(low: Double, high: Double): Double = js.native
  /**
     * Random float from - range / 2 to range / 2 interval.
     */
  def randFloatSpread(range: Double): Double = js.native
  def degToRad(degrees: Double): Double = js.native
  def radToDeg(radians: Double): Double = js.native
  def isPowerOfTwo(value: Double): Boolean = js.native
  /**
     * @deprecated Use { Math#floorPowerOfTwo .floorPowerOfTwo()}
     */
  def nearestPowerOfTwo(value: Double): Double = js.native
  /**
     * @deprecated Use { Math#ceilPowerOfTwo .ceilPowerOfTwo()}
     */
  def nextPowerOfTwo(value: Double): Double = js.native
  def floorPowerOfTwo(value: Double): Double = js.native
  def ceilPowerOfTwo(value: Double): Double = js.native
}