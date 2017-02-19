package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Line3")
class Line3(var start: Vector3 = js.native, var end: Vector3 = js.native)
    extends js.Object {
  def set(start: Vector3 = js.native, end: Vector3 = js.native): Line3 = js.native
  def copy(line: Line3): Line3                                         = js.native
  def center(optionalTarget: Vector3 = js.native): Vector3             = js.native
  def delta(optionalTarget: Vector3 = js.native): Vector3              = js.native
  def distanceSq(): Double                                             = js.native
  def distance(): Double                                               = js.native
  def at(t: Double, optionalTarget: Vector3 = js.native): Vector3      = js.native
  def closestPointToPointParameter(point: Vector3,
                                   clampToLine: Boolean = js.native): Double = js.native
  def closestPointToPoint(point: Vector3,
                          clampToLine: Boolean = js.native,
                          optionalTarget: Vector3 = js.native): Vector3 = js.native
  def applyMatrix4(matrix: Matrix4): Line3                              = js.native
  def equals(line: Line3): Boolean                                      = js.native
  override def clone(): Line3                                           = js.native
}
