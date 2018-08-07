package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Scene")
class Scene extends Object3D {
  /**
     * A fog instance defining the type of fog that affects everything rendered in the scene. Default is null.
     */
  var fog: IFog = js.native
  /**
     * If not null, it will force everything in the scene to be rendered with that material. Default is null.
     */
  var overrideMaterial: Material = js.native
  var autoUpdate: Boolean = js.native
  var background: js.Any = js.native
  //def toJSON(meta: js.Any = js.native): js.Dynamic = js.native
}