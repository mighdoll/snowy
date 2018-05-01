package minithree3

import scala.scalajs.js
import com.definitelyscala.node.Buffer
import scala.scalajs.js.typedarray._

  /**
     * clone():T;
     */
@js.native
@js.annotation.JSGlobal("Matrix3")
class Matrix3 extends Matrix {
  /**
     * Creates an identity matrix.
     */
  /**
     * Float32Array with matrix values.
     */
  var elements: Float32Array = js.native
  def set(n11: Double, n12: Double, n13: Double, n21: Double, n22: Double, n23: Double, n31: Double, n32: Double, n33: Double): Matrix3 = js.native
  def identity(): Matrix3 = js.native
  def copy(m: Matrix3): js.Dynamic = js.native
  def setFromMatrix4(m: Matrix4): Matrix3 = js.native
  /**
     * @deprecated Use { Matrix3#applyToBufferAttribute matrix3.applyToBufferAttribute( attribute )} instead.
     */
  def applyToBuffer(buffer: BufferAttribute, offset: Double = js.native, length: Double = js.native): BufferAttribute = js.native
  def applyToBufferAttribute(attribute: BufferAttribute): BufferAttribute = js.native
  def multiplyScalar(s: Double): Matrix3 = js.native
  def determinant(): Double = js.native
  def getInverse(matrix: Matrix3, throwOnDegenerate: Boolean): Matrix3 = js.native
  /**
     * Transposes this matrix in place.
     */
  def transpose(): Matrix3 = js.native
  def getNormalMatrix(matrix4: Matrix4): Matrix3 = js.native
  /**
     * Transposes this matrix into the supplied array r, and returns itself.
     */
  def transposeIntoArray(r: js.Array[Double]): js.Array[Double] = js.native
  def fromArray(array: js.Array[Double], offset: Double = js.native): Matrix3 = js.native
  def toArray(): js.Array[Double] = js.native
  /**
     * Multiplies this matrix by m.
     */
  def multiply(m: Matrix3): Matrix3 = js.native
  def premultiply(m: Matrix3): Matrix3 = js.native
  /**
     * Sets this matrix to a x b.
     */
  def multiplyMatrices(a: Matrix3, b: Matrix3): Matrix3 = js.native
  /**
     * @deprecated Use { Vector3.applyMatrix3 vector.applyMatrix3( matrix )} instead.
     */
  def multiplyVector3(vector: Vector3): js.Dynamic = js.native
  /**
     * @deprecated This method has been removed completely.
     */
  def multiplyVector3Array(a: js.Any): js.Dynamic = js.native
  def getInverse(matrix: Matrix4, throwOnDegenerate: Boolean): Matrix3 = js.native
  /**
     * @deprecated Use { Matrix3#toArray .toArray()} instead.
     */
  def flattenToArrayOffset(array: js.Array[Double], offset: Double): js.Array[Double] = js.native
}