package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Sphere")
class Sphere protected () extends js.Object {
  def this(center: Vector3 = js.native, radius: Double = js.native) = this()
  var center: Vector3 = js.native
  var radius: Double = js.native
  def set(center: Vector3, radius: Double): Sphere = js.native
  def setFromPoints(points: js.Array[Vector3], optionalCenter: Vector3 = js.native): Sphere = js.native
  def copy(sphere: Sphere): js.Dynamic = js.native
  def empty(): Boolean = js.native
  def containsPoint(point: Vector3): Boolean = js.native
  def distanceToPoint(point: Vector3): Double = js.native
  def intersectsSphere(sphere: Sphere): Boolean = js.native
  def intersectsBox(box: Box3): Boolean = js.native
  def intersectsPlane(plane: Plane): Boolean = js.native
  def clampPoint(point: Vector3, target: Vector3): Vector3 = js.native
  def getBoundingBox(target: Box3): Box3 = js.native
  def applyMatrix4(matrix: Matrix4): Sphere = js.native
  def translate(offset: Vector3): Sphere = js.native
  def equals(sphere: Sphere): Boolean = js.native
}