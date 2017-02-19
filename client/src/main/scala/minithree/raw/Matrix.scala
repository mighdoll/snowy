package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait Matrix extends js.Object {
  var elements: scala.scalajs.js.typedarray.Float32Array = js.native
  def identity(): Matrix                                 = js.native
  def copy(m: Matrix): Matrix                            = js.native
  def multiplyScalar(s: Double): Matrix                  = js.native
  def determinant(): Double                              = js.native
  def getInverse(matrix: Matrix, throwOnInvertible: Boolean = js.native): Matrix =
    js.native
  def transpose(): Matrix      = js.native
  override def clone(): Matrix = js.native
}

@js.native
@JSName("THREE.Matrix3")
class Matrix3 extends Matrix {

  def set(n11: Double,
          n12: Double,
          n13: Double,
          n21: Double,
          n22: Double,
          n23: Double,
          n31: Double,
          n32: Double,
          n33: Double): Matrix3    = js.native
  override def identity(): Matrix3 = js.native
  def copy(m: Matrix3): Matrix3    = js.native
  def applyToVector3Array(array: js.Array[Double],
                          offset: Double = js.native,
                          length: Double = js.native): js.Array[Double] = js.native
  override def multiplyScalar(s: Double): Matrix3                       = js.native
  override def determinant(): Double                                    = js.native
  def getInverse(matrix: Matrix3): Matrix3                              = js.native
  def getInverse(matrix: Matrix3, throwOnInvertible: Boolean): Matrix3  = js.native
  override def transpose(): Matrix3                                     = js.native
  def flattenToArrayOffset(array: js.Array[Double], offset: Double): js.Array[Double] =
    js.native
  def getNormalMatrix(m: Matrix4): Matrix3                      = js.native
  def transposeIntoArray(r: js.Array[Double]): js.Array[Double] = js.native
  def fromArray(array: js.Array[Double]): Matrix3               = js.native
  def toArray(): js.Array[Double]                               = js.native
  override def clone(): Matrix3                                 = js.native
}

@js.native
@JSName("THREE.Matrix4")
class Matrix4 extends Matrix {
  def set(n11: Double,
          n12: Double,
          n13: Double,
          n14: Double,
          n21: Double,
          n22: Double,
          n23: Double,
          n24: Double,
          n31: Double,
          n32: Double,
          n33: Double,
          n34: Double,
          n41: Double,
          n42: Double,
          n43: Double,
          n44: Double): Matrix4                                             = js.native
  override def identity(): Matrix4                                          = js.native
  def copy(m: Matrix4): Matrix4                                             = js.native
  def copyPosition(m: Matrix4): Matrix4                                     = js.native
  def extractRotation(m: Matrix4): Matrix4                                  = js.native
  def makeRotationFromEuler(euler: Euler): Matrix4                          = js.native
  def makeRotationFromQuaternion(q: Quaternion): Matrix4                    = js.native
  def lookAt(eye: Vector3, target: Vector3, up: Vector3): Matrix4           = js.native
  def multiply(m: Matrix4): Matrix4                                         = js.native
  def multiplyMatrices(a: Matrix4, b: Matrix4): Matrix4                     = js.native
  def multiplyToArray(a: Matrix4, b: Matrix4, r: js.Array[Double]): Matrix4 = js.native
  override def multiplyScalar(s: Double): Matrix4                           = js.native
  def applyToVector3Array(array: js.Array[Double],
                          offset: Double = js.native,
                          length: Double = js.native): js.Array[Double] = js.native
  override def determinant(): Double                                    = js.native
  override def transpose(): Matrix4                                     = js.native
  def flattenToArrayOffset(array: js.Array[Double], offset: Double): js.Array[Double] =
    js.native
  def setPosition(v: Vector3): Vector3                            = js.native
  def getInverse(m: Matrix4): Matrix4                             = js.native
  def getInverse(m: Matrix4, throwOnInvertible: Boolean): Matrix4 = js.native
  def scale(v: Vector3): Matrix4                                  = js.native
  def getMaxScaleOnAxis(): Double                                 = js.native
  def makeTranslation(x: Double, y: Double, z: Double): Matrix4   = js.native
  def makeRotationX(theta: Double): Matrix4                       = js.native
  def makeRotationY(theta: Double): Matrix4                       = js.native
  def makeRotationZ(theta: Double): Matrix4                       = js.native
  def makeRotationAxis(axis: Vector3, angle: Double): Matrix4     = js.native
  def makeScale(x: Double, y: Double, z: Double): Matrix4         = js.native
  def compose(translation: Vector3, rotation: Quaternion, scale: Vector3): Matrix4 =
    js.native
  def decompose(translation: Vector3 = js.native,
                rotation: Quaternion = js.native,
                scale: Vector3 = js.native): js.Array[Object] = js.native
  def makeFrustum(left: Double,
                  right: Double,
                  bottom: Double,
                  top: Double,
                  near: Double,
                  far: Double): Matrix4 = js.native
  def makePerspective(fov: Double, aspect: Double, near: Double, far: Double): Matrix4 =
    js.native
  def makeOrthographic(left: Double,
                       right: Double,
                       top: Double,
                       bottom: Double,
                       near: Double,
                       far: Double): Matrix4      = js.native
  def fromArray(array: js.Array[Double]): Matrix4 = js.native
  def toArray(): js.Array[Double]                 = js.native
  override def clone(): Matrix4                   = js.native
}
