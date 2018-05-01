package minithree3.SceneUtils

import scala.scalajs.js
@js.native
@js.annotation.JSGlobal("SceneUtils")
object SceneUtils extends js.Object {
  def createMultiMaterialObject(geometry: Geometry, materials: js.Array[Material]): Object3D = js.native
  def detach(child: Object3D, parent: Object3D, scene: Scene): Unit = js.native
  def attach(child: Object3D, scene: Scene, parent: Object3D): Unit = js.native
}