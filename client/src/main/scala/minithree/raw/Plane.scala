package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Plane")
class Plane(var normal: Vector3 = js.native, var constant: Double = js.native)
    extends js.Object {
  def set(normal: Vector3, constant: Double): Plane                         = js.native
  def setComponents(x: Double, y: Double, z: Double, w: Double): Plane      = js.native
  def setFromNormalAndCoplanarPoint(normal: Vector3, point: Vector3): Plane = js.native
  def setFromCoplanarPoints(a: Vector3, b: Vector3, c: Vector3): Plane      = js.native
  def copy(plane: Plane): Plane                                             = js.native
  def normalize(): Plane                                                    = js.native
  def negate(): Plane                                                       = js.native
  def distanceToPoint(point: Vector3): Double                               = js.native
  def distanceToSphere(sphere: Sphere): Double                              = js.native
  def projectPoint(point: Vector3, optionalTarget: Vector3 = js.native): Vector3 =
    js.native
  def orthoPoint(point: Vector3, optionalTarget: Vector3 = js.native): Vector3 =
    js.native
  def isIntersectionLine(line: Line3): Boolean = js.native
  def intersectLine(line: Line3, optionalTarget: Vector3 = js.native): Vector3 =
    js.native
  def coplanarPoint(optionalTarget: Boolean = js.native): Vector3 = js.native
  def applyMatrix4(matrix: Matrix4, optionalNormalMatrix: Matrix3 = js.native): Plane =
    js.native
  def translate(offset: Vector3): Plane = js.native
  def equals(plane: Plane): Boolean     = js.native
  override def clone(): Plane           = js.native
}
