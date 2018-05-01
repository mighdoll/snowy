package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Line3")
class Line3 protected () extends js.Object {
  def this(start: Vector3 = js.native, end: Vector3 = js.native) = this()
  var start: Vector3 = js.native
  var end: Vector3 = js.native
  def set(start: Vector3 = js.native, end: Vector3 = js.native): Line3 = js.native
  def copy(line: Line3): js.Dynamic = js.native
  def getCenter(target: Vector3): Vector3 = js.native
  def delta(target: Vector3): Vector3 = js.native
  def distanceSq(): Double = js.native
  def distance(): Double = js.native
  def at(t: Double, target: Vector3): Vector3 = js.native
  def closestPointToPointParameter(point: Vector3, clampToLine: Boolean = js.native): Double = js.native
  def closestPointToPoint(point: Vector3, clampToLine: Boolean, target: Vector3): Vector3 = js.native
  def applyMatrix4(matrix: Matrix4): Line3 = js.native
  def equals(line: Line3): Boolean = js.native
}