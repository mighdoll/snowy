package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait RaycasterParameters extends js.Object {
  var Sprite: js.Any     = js.native
  var Mesh: js.Any       = js.native
  var PointCloud: js.Any = js.native
  var LOD: js.Any        = js.native
  var Line: js.Any       = js.native
}

@js.native
@JSName("THREE.Raycaster")
class Raycaster(origin: Vector3 = js.native,
                direction: Vector3 = js.native,
                var near: Double = js.native,
                var far: Double = js.native)
    extends js.Object {
  var ray: Ray                                             = js.native
  var params: RaycasterParameters                          = js.native
  var precision: Double                                    = js.native
  var linePrecision: Double                                = js.native
  def set(origin: Vector3, direction: Vector3): Unit       = js.native
  def setFromCamera(origin: Vector2, camera: Camera): Unit = js.native
  def setFromCamera(origin: Vector3, camera: Camera): Unit = js.native
  def intersectObject(`object`: Object3D,
                      recursive: Boolean = js.native): js.Array[Intersection] = js.native
  def intersectObjects(objects: js.Array[Object3D],
                       recursive: Boolean = js.native): js.Array[Intersection] =
    js.native
}
