package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Frustum")
class Frustum protected () extends js.Object {
  def this(p0: Plane = js.native, p1: Plane = js.native, p2: Plane = js.native, p3: Plane = js.native, p4: Plane = js.native, p5: Plane = js.native) = this()
  /**
     * Array of 6 vectors.
     */
  var planes: js.Array[Plane] = js.native
  def set(p0: Double = js.native, p1: Double = js.native, p2: Double = js.native, p3: Double = js.native, p4: Double = js.native, p5: Double = js.native): Frustum = js.native
  def copy(frustum: Frustum): js.Dynamic = js.native
  def setFromMatrix(m: Matrix4): Frustum = js.native
  def intersectsObject(`object`: Object3D): Boolean = js.native
  def intersectsObject(sprite: Sprite): Boolean = js.native
  def intersectsSphere(sphere: Sphere): Boolean = js.native
  def intersectsBox(box: Box3): Boolean = js.native
  def containsPoint(point: Vector3): Boolean = js.native
}