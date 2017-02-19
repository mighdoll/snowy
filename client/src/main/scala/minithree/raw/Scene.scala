package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Scene")
class Scene extends Object3D {
  var fog: IFog                  = js.native
  var overrideMaterial: Material = js.native
  var autoUpdate: Boolean        = js.native
}
