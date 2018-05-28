package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("Light")
class Light protected () extends Object3D {
  def this(hex: Double | String = js.native, intensity: Double = js.native) = this()
  var color: Color = js.native
  var intensity: Double = js.native
  //var receiveShadow: Boolean = js.native
  var shadow: LightShadow = js.native
  /**
     * @deprecated Use shadow.camera.fov instead.
     */
  var shadowCameraFov: js.Any = js.native
  /**
     * @deprecated Use shadow.camera.left instead.
     */
  var shadowCameraLeft: js.Any = js.native
  /**
     * @deprecated Use shadow.camera.right instead.
     */
  var shadowCameraRight: js.Any = js.native
  /**
     * @deprecated Use shadow.camera.top instead.
     */
  var shadowCameraTop: js.Any = js.native
  /**
     * @deprecated Use shadow.camera.bottom instead.
     */
  var shadowCameraBottom: js.Any = js.native
  /**
     * @deprecated Use shadow.camera.near instead.
     */
  var shadowCameraNear: js.Any = js.native
  /**
     * @deprecated Use shadow.camera.far instead.
     */
  var shadowCameraFar: js.Any = js.native
  /**
     * @deprecated Use shadow.bias instead.
     */
  var shadowBias: js.Any = js.native
  /**
     * @deprecated Use shadow.mapSize.width instead.
     */
  var shadowMapWidth: js.Any = js.native
  /**
     * @deprecated Use shadow.mapSize.height instead.
     */
  var shadowMapHeight: js.Any = js.native
}