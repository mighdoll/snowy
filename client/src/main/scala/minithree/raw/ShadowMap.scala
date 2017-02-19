package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSBracketAccess, JSName}

@js.native
@JSName("THREE.ShadowMapPlugin")
class ShadowMapPlugin extends RendererPlugin {
  def init(renderer: Renderer): Unit             = js.native
  def render(scene: Scene, camera: Camera): Unit = js.native
  def update(scene: Scene, camera: Camera): Unit = js.native
}

@js.native
sealed trait ShadowMapType extends js.Object {}

@js.native
@JSName("THREE.ShadowMapType")
object ShadowMapType extends js.Object {
  @JSBracketAccess
  def apply(value: ShadowMapType): String = js.native
}
