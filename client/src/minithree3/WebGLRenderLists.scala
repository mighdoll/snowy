package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLRenderLists")
class WebGLRenderLists extends js.Object {
  def dispose(): Unit = js.native
  /**
    *
    * returns {<String> : <WebGLRenderList>}
    */
  def get(scene: Scene, camera: Camera): WebGLRenderList = js.native
}