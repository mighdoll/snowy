package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Box3")
class Box3 protected () extends js.Object {
  def this(min: Vector3 = js.native, max: Vector3 = js.native) = this()
  var max: Vector3 = js.native
  var min: Vector3 = js.native
  def set(min: Vector3, max: Vector3): Box3 = js.native
  def setFromArray(array: ArrayLike[Double]): Box3 = js.native
  def setFromPoints(points: js.Array[Vector3]): Box3 = js.native
  def setFromCenterAndSize(center: Vector3, size: Vector3): Box3 = js.native
  def setFromObject(`object`: Object3D): Box3 = js.native
  def copy(box: Box3): js.Dynamic = js.native
  def makeEmpty(): Box3 = js.native
  def isEmpty(): Boolean = js.native
  def getCenter(target: Vector3): Vector3 = js.native
  def getSize(target: Vector3): Vector3 = js.native
  def expandByPoint(point: Vector3): Box3 = js.native
  def expandByVector(vector: Vector3): Box3 = js.native
  def expandByScalar(scalar: Double): Box3 = js.native
  def expandByObject(`object`: Object3D): Box3 = js.native
  def containsPoint(point: Vector3): Boolean = js.native
  def containsBox(box: Box3): Boolean = js.native
  def getParameter(point: Vector3): Vector3 = js.native
  def intersectsBox(box: Box3): Boolean = js.native
  def intersectsSphere(sphere: Sphere): Boolean = js.native
  def intersectsPlane(plane: Plane): Boolean = js.native
  def clampPoint(point: Vector3, target: Vector3): Vector3 = js.native
  def distanceToPoint(point: Vector3): Double = js.native
  def getBoundingSphere(target: Sphere): Sphere = js.native
  def intersect(box: Box3): Box3 = js.native
  def union(box: Box3): Box3 = js.native
  def applyMatrix4(matrix: Matrix4): Box3 = js.native
  def translate(offset: Vector3): Box3 = js.native
  def equals(box: Box3): Boolean = js.native
  /**
     * @deprecated Use { Box3#isEmpty .isEmpty()} instead.
     */
  def empty(): js.Dynamic = js.native
  /**
     * @deprecated Use { Box3#intersectsBox .intersectsBox()} instead.
     */
  def isIntersectionBox(b: js.Any): js.Dynamic = js.native
  /**
     * @deprecated Use { Box3#intersectsSphere .intersectsSphere()} instead.
     */
  def isIntersectionSphere(s: js.Any): js.Dynamic = js.native
}