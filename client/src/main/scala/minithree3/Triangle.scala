package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Triangle")
class Triangle protected () extends js.Object {
  def this(a: Vector3 = js.native, b: Vector3 = js.native, c: Vector3 = js.native) = this()
  var a: Vector3 = js.native
  var b: Vector3 = js.native
  var c: Vector3 = js.native
  def set(a: Vector3, b: Vector3, c: Vector3): Triangle = js.native
  def setFromPointsAndIndices(points: js.Array[Vector3], i0: Double, i1: Double, i2: Double): Triangle = js.native
  def copy(triangle: Triangle): js.Dynamic = js.native
  def getArea(): Double = js.native
  def getMidpoint(target: Vector3): Vector3 = js.native
  def getNormal(target: Vector3): Vector3 = js.native
  def getPlane(target: Vector3): Plane = js.native
  def getBarycoord(point: Vector3, target: Vector3): Vector3 = js.native
  def containsPoint(point: Vector3): Boolean = js.native
  def closestPointToPoint(point: Vector3, target: Vector3): Vector3 = js.native
  def equals(triangle: Triangle): Boolean = js.native
}

@js.native
@js.annotation.JSGlobal("Triangle")
object Triangle extends js.Object {
  def getNormal(a: Vector3, b: Vector3, c: Vector3, target: Vector3): Vector3 = js.native
  def getBarycoord(point: Vector3, a: Vector3, b: Vector3, c: Vector3, target: Vector3): Vector3 = js.native
  def containsPoint(point: Vector3, a: Vector3, b: Vector3, c: Vector3): Boolean = js.native
}