package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLShadowMap")
class WebGLShadowMap protected () extends js.Object {
  def this(_renderer: Renderer, _lights: js.Array[js.Any], _objects: js.Array[js.Any], capabilities: js.Any) = this()
  var enabled: Boolean = js.native
  var autoUpdate: Boolean = js.native
  var needsUpdate: Boolean = js.native
  var `type`: ShadowMapType = js.native
  def render(scene: Scene, camera: Camera): Unit = js.native
  /**
     * @deprecated Use { WebGLShadowMap#renderReverseSided .shadowMap.renderReverseSided} instead.
     */
  var cullFace: js.Any = js.native
}