package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait Vector extends js.Object {
  def setComponent(index: Double, value: Double): Unit = js.native
  def getComponent(index: Double): Double              = js.native
  def copy(v: Vector): Vector                          = js.native
  def add(v: Vector): Vector                           = js.native
  def addVectors(a: Vector, b: Vector): Vector         = js.native
  def sub(v: Vector): Vector                           = js.native
  def subVectors(a: Vector, b: Vector): Vector         = js.native
  def multiplyScalar(s: Double): Vector                = js.native
  def divideScalar(s: Double): Vector                  = js.native
  def negate(): Vector                                 = js.native
  def dot(v: Vector): Double                           = js.native
  def lengthSq(): Double                               = js.native
  def length(): Double                                 = js.native
  def normalize(): Vector                              = js.native
  def distanceTo(v: Vector): Double                    = js.native
  def distanceToSquared(v: Vector): Double             = js.native
  def setLength(l: Double): Vector                     = js.native
  def lerp(v: Vector, alpha: Double): Vector           = js.native
  def equals(v: Vector): Boolean                       = js.native
  override def clone(): Vector                         = js.native
}

@js.native
@JSName("THREE.Vector2")
class Vector2(var x: Double = js.native, var y: Double = js.native) extends Vector {
  def set(x: Double, y: Double): Vector2                        = js.native
  def setX(x: Double): Vector2                                  = js.native
  def setY(y: Double): Vector2                                  = js.native
  override def setComponent(index: Double, value: Double): Unit = js.native
  override def getComponent(index: Double): Double              = js.native
  def copy(v: Vector2): Vector2                                 = js.native
  def add(v: Vector2): Vector2                                  = js.native
  def addVectors(a: Vector2, b: Vector2): Vector2               = js.native
  def addScalar(s: Double): Vector2                             = js.native
  def sub(v: Vector2): Vector2                                  = js.native
  def subVectors(a: Vector2, b: Vector2): Vector2               = js.native
  def multiply(v: Vector2): Vector2                             = js.native
  override def multiplyScalar(s: Double): Vector2               = js.native
  def divide(v: Vector2): Vector2                               = js.native
  override def divideScalar(s: Double): Vector2                 = js.native
  def min(v: Vector2): Vector2                                  = js.native
  def max(v: Vector2): Vector2                                  = js.native
  def clamp(min: Vector2, max: Vector2): Vector2                = js.native
  def clampScalar(min: Double, max: Double): Vector2            = js.native
  def floor(): Vector2                                          = js.native
  def ceil(): Vector2                                           = js.native
  def round(): Vector2                                          = js.native
  def roundToZero(): Vector2                                    = js.native
  override def negate(): Vector2                                = js.native
  def dot(v: Vector2): Double                                   = js.native
  override def lengthSq(): Double                               = js.native
  override def length(): Double                                 = js.native
  override def normalize(): Vector2                             = js.native
  def distanceTo(v: Vector2): Double                            = js.native
  def distanceToSquared(v: Vector2): Double                     = js.native
  override def setLength(l: Double): Vector2                    = js.native
  def lerp(v: Vector2, alpha: Double): Vector2                  = js.native
  def equals(v: Vector2): Boolean                               = js.native
  def fromArray(xy: js.Array[Double]): Vector2                  = js.native
  def toArray(): js.Array[Double]                               = js.native
  override def clone(): Vector2                                 = js.native
}

@js.native
@JSName("THREE.Vector3")
class Vector3(var x: Double = js.native,
              var y: Double = js.native,
              var z: Double = js.native)
    extends Vector {
  def set(x: Double, y: Double, z: Double): Vector3                = js.native
  def setX(x: Double): Vector3                                     = js.native
  def setY(y: Double): Vector3                                     = js.native
  def setZ(z: Double): Vector3                                     = js.native
  override def setComponent(index: Double, value: Double): Unit    = js.native
  override def getComponent(index: Double): Double                 = js.native
  def copy(v: Vector3): Vector3                                    = js.native
  def add(a: Vector3): Vector3                                     = js.native
  def addScalar(s: Double): Vector3                                = js.native
  def addVectors(a: Vector3, b: Vector3): Vector3                  = js.native
  def sub(a: Vector3): Vector3                                     = js.native
  def subVectors(a: Vector3, b: Vector3): Vector3                  = js.native
  def multiply(v: Vector3): Vector3                                = js.native
  override def multiplyScalar(s: Double): Vector3                  = js.native
  def multiplyVectors(a: Vector3, b: Vector3): Vector3             = js.native
  def applyEuler(euler: Euler): Vector3                            = js.native
  def applyAxisAngle(axis: Vector3, angle: Double): Vector3        = js.native
  def applyMatrix3(m: Matrix3): Vector3                            = js.native
  def applyMatrix4(m: Matrix4): Vector3                            = js.native
  def applyProjection(m: Matrix4): Vector3                         = js.native
  def applyQuaternion(q: Quaternion): Vector3                      = js.native
  def transformDirection(m: Matrix4): Vector3                      = js.native
  def divide(v: Vector3): Vector3                                  = js.native
  override def divideScalar(s: Double): Vector3                    = js.native
  def min(v: Vector3): Vector3                                     = js.native
  def max(v: Vector3): Vector3                                     = js.native
  def clamp(min: Vector3, max: Vector3): Vector3                   = js.native
  def clampScalar(min: Double, max: Double): Vector3               = js.native
  def floor(): Vector3                                             = js.native
  def ceil(): Vector3                                              = js.native
  def round(): Vector3                                             = js.native
  def roundToZero(): Vector3                                       = js.native
  override def negate(): Vector3                                   = js.native
  def dot(v: Vector3): Double                                      = js.native
  override def lengthSq(): Double                                  = js.native
  override def length(): Double                                    = js.native
  def lengthManhattan(): Double                                    = js.native
  override def normalize(): Vector3                                = js.native
  override def setLength(l: Double): Vector3                       = js.native
  def lerp(v: Vector3, alpha: Double): Vector3                     = js.native
  def cross(a: Vector3): Vector3                                   = js.native
  def crossVectors(a: Vector3, b: Vector3): Vector3                = js.native
  def project(camera: Camera): Vector3                             = js.native
  def unproject(camera: Camera): Vector3                           = js.native
  def projectOnVector(v: Vector3): Vector3                         = js.native
  def projectOnPlane(planeNormal: Vector3): Vector3                = js.native
  def reflect(vector: Vector3): Vector3                            = js.native
  def angleTo(v: Vector3): Double                                  = js.native
  def distanceTo(v: Vector3): Double                               = js.native
  def distanceToSquared(v: Vector3): Double                        = js.native
  def setFromMatrixPosition(m: Matrix4): Vector3                   = js.native
  def setFromMatrixScale(m: Matrix4): Vector3                      = js.native
  def setFromSpherical(s: Spherical): Vector3                      = js.native
  def setFromMatrixColumn(matrix: Matrix4, index: Double): Vector3 = js.native
  def equals(v: Vector3): Boolean                                  = js.native
  def fromArray(xyz: js.Array[Double]): Vector3                    = js.native
  def toArray(): js.Array[Double]                                  = js.native
  override def clone(): Vector3                                    = js.native
}
