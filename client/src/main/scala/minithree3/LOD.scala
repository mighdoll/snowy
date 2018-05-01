package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LOD")
class LOD extends Object3D {
  var levels: js.Array[js.Any] = js.native
  def addLevel(`object`: Object3D, distance: Double = js.native): Unit = js.native
  def getObjectForDistance(distance: Double): Object3D = js.native
  def raycast(raycaster: Raycaster, intersects: js.Any): Unit = js.native
  def update(camera: Camera): Unit = js.native
  def toJSON(meta: js.Any): js.Dynamic = js.native
  /**
     * @deprecated Use { LOD#levels .levels} instead.
     */
  var objects: js.Array[js.Any] = js.native
}