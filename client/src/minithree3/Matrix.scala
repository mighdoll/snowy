package minithree3

import scala.scalajs.js
import scala.scalajs.js.typedarray._

@js.native
trait Matrix extends js.Object {
  /**
     * Float32Array with matrix values.
     */
  var elements: Float32Array = js.native
  /**
     * identity():T;
     */
  def identity(): Matrix = js.native
  /**
     * copy(m:T):T;
     */
  def copy(m: Matrix): js.Dynamic = js.native
  /**
     * multiplyScalar(s:number):T;
     */
  def multiplyScalar(s: Double): Matrix = js.native
  def determinant(): Double = js.native
  /**
     * getInverse(matrix:T, throwOnInvertible?:boolean):T;
     */
  def getInverse(matrix: Matrix, throwOnInvertible: Boolean = js.native): Matrix = js.native
  /**
     * transpose():T;
     */
  def transpose(): Matrix = js.native
}