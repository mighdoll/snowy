package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Ray")
class Ray protected () extends js.Object {
  def this(origin: Vector3 = js.native, direction: Vector3 = js.native) = this()
  var origin: Vector3 = js.native
  var direction: Vector3 = js.native
  def set(origin: Vector3, direction: Vector3): Ray = js.native
  def copy(ray: Ray): js.Dynamic = js.native
  def at(t: Double, target: Vector3): Vector3 = js.native
  def lookAt(v: Vector3): Vector3 = js.native
  def recast(t: Double): Ray = js.native
  def closestPointToPoint(point: Vector3, target: Vector3): Vector3 = js.native
  def distanceToPoint(point: Vector3): Double = js.native
  def distanceSqToPoint(point: Vector3): Double = js.native
  def distanceSqToSegment(v0: Vector3, v1: Vector3, optionalPointOnRay: Vector3 = js.native, optionalPointOnSegment: Vector3 = js.native): Double = js.native
  def intersectSphere(sphere: Sphere, target: Vector3): Vector3 = js.native
  def intersectsSphere(sphere: Sphere): Boolean = js.native
  def distanceToPlane(plane: Plane): Double = js.native
  def intersectPlane(plane: Plane, target: Vector3): Vector3 = js.native
  def intersectsPlane(plane: Plane): Boolean = js.native
  def intersectBox(box: Box3, target: Vector3): Vector3 = js.native
  def intersectsBox(box: Box3): Boolean = js.native
  def intersectTriangle(a: Vector3, b: Vector3, c: Vector3, backfaceCulling: Boolean, target: Vector3): Vector3 = js.native
  def applyMatrix4(matrix4: Matrix4): Ray = js.native
  def equals(ray: Ray): Boolean = js.native
  /**
     * @deprecated Use { Ray#intersectsBox .intersectsBox()} instead.
     */
  def isIntersectionBox(b: js.Any): js.Dynamic = js.native
  /**
     * @deprecated Use { Ray#intersectsPlane .intersectsPlane()} instead.
     */
  def isIntersectionPlane(p: js.Any): js.Dynamic = js.native
  /**
     * @deprecated Use { Ray#intersectsSphere .intersectsSphere()} instead.
     */
  def isIntersectionSphere(s: js.Any): js.Dynamic = js.native
}