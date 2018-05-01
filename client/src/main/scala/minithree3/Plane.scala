package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Plane")
class Plane protected () extends js.Object {
  def this(normal: Vector3 = js.native, constant: Double = js.native) = this()
  var normal: Vector3 = js.native
  var constant: Double = js.native
  def set(normal: Vector3, constant: Double): Plane = js.native
  def setComponents(x: Double, y: Double, z: Double, w: Double): Plane = js.native
  def setFromNormalAndCoplanarPoint(normal: Vector3, point: Vector3): Plane = js.native
  def setFromCoplanarPoints(a: Vector3, b: Vector3, c: Vector3): Plane = js.native
  def copy(plane: Plane): js.Dynamic = js.native
  def normalize(): Plane = js.native
  def negate(): Plane = js.native
  def distanceToPoint(point: Vector3): Double = js.native
  def distanceToSphere(sphere: Sphere): Double = js.native
  def projectPoint(point: Vector3, target: Vector3): Vector3 = js.native
  def orthoPoint(point: Vector3, target: Vector3): Vector3 = js.native
  def intersectLine(line: Line3, target: Vector3): Vector3 = js.native
  def intersectsLine(line: Line3): Boolean = js.native
  def intersectsBox(box: Box3): Boolean = js.native
  def coplanarPoint(target: Vector3): Vector3 = js.native
  def applyMatrix4(matrix: Matrix4, optionalNormalMatrix: Matrix3 = js.native): Plane = js.native
  def translate(offset: Vector3): Plane = js.native
  def equals(plane: Plane): Boolean = js.native
  /**
     * @deprecated Use { Plane#intersectsLine .intersectsLine()} instead.
     */
  def isIntersectionLine(l: js.Any): js.Dynamic = js.native
}