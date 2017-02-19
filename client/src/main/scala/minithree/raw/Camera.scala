package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Camera")
class Camera extends Object3D {
  var matrixWorldInverse: Matrix4            = js.native
  var projectionMatrix: Matrix4              = js.native
  override def lookAt(vector: Vector3): Unit = js.native
  def clone(camera: Camera): Camera          = js.native
}
