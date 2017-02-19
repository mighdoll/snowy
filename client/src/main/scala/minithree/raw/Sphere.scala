package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Sphere")
class Sphere(var center: Vector3 = js.native, var radius: Double = js.native)
    extends js.Object {
  def set(center: Vector3, radius: Double): Sphere = js.native
  def setFromPoints(points: js.Array[Vector3],
                    optionalCenter: Vector3 = js.native): Sphere = js.native
  def copy(sphere: Sphere): Sphere                               = js.native
  def empty(): Boolean                                           = js.native
  def containsPoint(point: Vector3): Boolean                     = js.native
  def distanceToPoint(point: Vector3): Double                    = js.native
  def intersectsSphere(sphere: Sphere): Boolean                  = js.native
  def clampPoint(point: Vector3, optionalTarget: Vector3 = js.native): Vector3 =
    js.native
  def getBoundingBox(optionalTarget: Box3 = js.native): Box3 = js.native
  def applyMatrix4(matrix: Matrix4): Sphere                  = js.native
  def translate(offset: Vector3): Sphere                     = js.native
  def equals(sphere: Sphere): Boolean                        = js.native
  override def clone(): Sphere                               = js.native
}
