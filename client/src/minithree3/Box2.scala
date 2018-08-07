package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Box2")
class Box2 protected () extends js.Object {
  def this(min: Vector2 = js.native, max: Vector2 = js.native) = this()
  var max: Vector2 = js.native
  var min: Vector2 = js.native
  def set(min: Vector2, max: Vector2): Box2 = js.native
  def setFromPoints(points: js.Array[Vector2]): Box2 = js.native
  def setFromCenterAndSize(center: Vector2, size: Vector2): Box2 = js.native
  def copy(box: Box2): js.Dynamic = js.native
  def makeEmpty(): Box2 = js.native
  def isEmpty(): Boolean = js.native
  def getCenter(target: Vector2): Vector2 = js.native
  def getSize(target: Vector2): Vector2 = js.native
  def expandByPoint(point: Vector2): Box2 = js.native
  def expandByVector(vector: Vector2): Box2 = js.native
  def expandByScalar(scalar: Double): Box2 = js.native
  def containsPoint(point: Vector2): Boolean = js.native
  def containsBox(box: Box2): Boolean = js.native
  def getParameter(point: Vector2): Vector2 = js.native
  def intersectsBox(box: Box2): Boolean = js.native
  def clampPoint(point: Vector2, target: Vector2): Vector2 = js.native
  def distanceToPoint(point: Vector2): Double = js.native
  def intersect(box: Box2): Box2 = js.native
  def union(box: Box2): Box2 = js.native
  def translate(offset: Vector2): Box2 = js.native
  def equals(box: Box2): Boolean = js.native
  /**
     * @deprecated Use { Box2#isEmpty .isEmpty()} instead.
     */
  def empty(): js.Dynamic = js.native
  /**
     * @deprecated Use { Box2#intersectsBox .intersectsBox()} instead.
     */
  def isIntersectionBox(b: js.Any): js.Dynamic = js.native
}