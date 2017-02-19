package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Ray")
class Ray(var origin: Vector3 = js.native, var direction: Vector3 = js.native)
    extends js.Object {
  def set(origin: Vector3, direction: Vector3): Ray               = js.native
  def copy(ray: Ray): Ray                                         = js.native
  def at(t: Double, optionalTarget: Vector3 = js.native): Vector3 = js.native
  def recast(t: Double): Ray                                      = js.native
  def closestPointToPoint(point: Vector3, optionalTarget: Vector3 = js.native): Vector3 =
    js.native
  def distanceToPoint(point: Vector3): Double = js.native
  def distanceSqToSegment(v0: Vector3,
                          v1: Vector3,
                          optionalPointOnRay: Vector3 = js.native,
                          optionalPointOnSegment: Vector3 = js.native): Double =
    js.native
  def isIntersectionSphere(sphere: Sphere): Boolean = js.native
  def intersectSphere(sphere: Sphere, optionalTarget: Vector3 = js.native): Vector3 =
    js.native
  def isIntersectionPlane(plane: Plane): Boolean = js.native
  def distanceToPlane(plane: Plane): Double      = js.native
  def intersectPlane(plane: Plane, optionalTarget: Vector3 = js.native): Vector3 =
    js.native
  def isIntersectionBox(box: Box3): Boolean                                 = js.native
  def intersectBox(box: Box3, optionalTarget: Vector3 = js.native): Vector3 = js.native
  def intersectTriangle(a: Vector3,
                        b: Vector3,
                        c: Vector3,
                        backfaceCulling: Boolean,
                        optionalTarget: Vector3 = js.native): Vector3 = js.native
  def applyMatrix4(matrix4: Matrix4): Ray                             = js.native
  def equals(ray: Ray): Boolean                                       = js.native
  override def clone(): Ray                                           = js.native
}
