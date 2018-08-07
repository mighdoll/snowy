package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLTextures")
class WebGLTextures protected () extends js.Object {
  def this(gl: js.Any, extensions: js.Any, state: js.Any, properties: js.Any, capabilities: js.Any, paramThreeToGL: js.Function, info: js.Any) = this()
  def setTexture2D(texture: js.Any, slot: Double): Unit = js.native
  def setTextureCube(texture: js.Any, slot: Double): Unit = js.native
  def setTextureCubeDynamic(texture: js.Any, slot: Double): Unit = js.native
  def setupRenderTarget(renderTarget: js.Any): Unit = js.native
  def updateRenderTargetMipmap(renderTarget: js.Any): Unit = js.native
}