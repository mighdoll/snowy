package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CubeCamera")
class CubeCamera protected () extends Object3D {
  def this(near: Double = js.native, far: Double = js.native, cubeResolution: Double = js.native) = this()
  var renderTarget: WebGLRenderTargetCube = js.native
  /**
     * @deprecated Use { CubeCamera#update .update()} instead
     */
  def updateCubeMap(renderer: Renderer, scene: Scene): Unit = js.native
  def update(renderer: Renderer, scene: Scene): Unit = js.native
}