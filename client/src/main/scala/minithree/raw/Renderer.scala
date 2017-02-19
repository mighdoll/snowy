package minithree.raw

import org.scalajs.dom.raw.HTMLCanvasElement

import scala.scalajs.js

@js.native
trait Renderer extends js.Object {
  var domElement: HTMLCanvasElement = js.native

  def render(scene: Scene, camera: Camera): Unit = js.native

  def setSize(width: Double, height: Double, updateStyle: Boolean = js.native): Unit =
    js.native
}

@js.native
trait RenderTarget extends js.Object {}

@js.native
trait RendererPlugin extends js.Object {
  def init(renderer: WebGLRenderer): Unit = js.native
  def render(scene: Scene,
             camera: Camera,
             currentWidth: Double,
             currentHeight: Double): Unit = js.native
}
