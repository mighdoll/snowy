package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
trait WebGLRendererParameters extends js.Object {
  /**
     * A Canvas where the renderer draws its output.
     */
  var canvas: HTMLCanvasElement = js.native
  /**
     *  shader precision. Can be "highp", "mediump" or "lowp".
     */
  var precision: String = js.native
  /**
     * default is true.
     */
  var alpha: Boolean = js.native
  /**
     * default is true.
     */
  var premultipliedAlpha: Boolean = js.native
  /**
     * default is false.
     */
  var antialias: Boolean = js.native
  /**
     * default is true.
     */
  var stencil: Boolean = js.native
  /**
     * default is false.
     */
  var preserveDrawingBuffer: Boolean = js.native
  /**
     * default is 0x000000.
     */
  var clearColor: Double = js.native
  /**
     * default is 0.
     */
  var clearAlpha: Double = js.native
  var devicePixelRatio: Double = js.native
  /**
     * default is false.
     */
  var logarithmicDepthBuffer: Boolean = js.native
}