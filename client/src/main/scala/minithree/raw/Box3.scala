package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Box3")
class Box3(var min: Vector3 = js.native, var max: Vector3 = js.native)
    extends js.Object {
  def set(min: Vector3, max: Vector3): Box3                     = js.native
  def setFromPoints(points: js.Array[Vector3]): Box3            = js.native
  def setFromCenterAndSize(center: Vector3, size: Double): Box3 = js.native
  def setFromObject(`object`: Object3D): Box3                   = js.native
  def copy(box: Box3): Box3                                     = js.native
  def makeEmpty(): Box3                                         = js.native
  def empty(): Boolean                                          = js.native
  def center(optionalTarget: Vector3 = js.native): Vector3      = js.native
  def size(optionalTarget: Vector3 = js.native): Vector3        = js.native
  def expandByPoint(point: Vector3): Box3                       = js.native
  def expandByVector(vector: Vector3): Box3                     = js.native
  def expandByScalar(scalar: Double): Box3                      = js.native
  def containsPoint(point: Vector3): Boolean                    = js.native
  def containsBox(box: Box3): Boolean                           = js.native
  def getParameter(point: Vector3): Vector3                     = js.native
  def isIntersectionBox(box: Box3): Boolean                     = js.native
  def clampPoint(point: Vector3, optionalTarget: Vector3 = js.native): Vector3 =
    js.native
  def distanceToPoint(point: Vector3): Double = js.native
  def getBoundingSphere(): Sphere             = js.native
  def intersect(box: Box3): Box3              = js.native
  def union(box: Box3): Box3                  = js.native
  def applyMatrix4(matrix: Matrix4): Box3     = js.native
  def translate(offset: Vector3): Box3        = js.native
  def equals(box: Box3): Boolean              = js.native
  override def clone(): Box3                  = js.native
}
