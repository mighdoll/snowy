package minithree3

import scala.scalajs.js


@js.native
@js.annotation.JSGlobal("Sprite")
class Sprite protected () extends Object3D {
  def this(material: Material = js.native) = this()
  var geometry: BufferGeometry = js.native
  var material: SpriteMaterial = js.native
  //def raycast(raycaster: Raycaster, intersects: js.Any): Unit = js.native
}