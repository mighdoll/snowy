package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait MaterialParameters extends js.Object {
  var alphaTest: Double = js.native
  var blendDst: BlendingDstFactor = js.native
  var blendDstAlpha: Double = js.native
  var blendEquation: BlendingEquation = js.native
  var blendEquationAlpha: Double = js.native
  var blending: Blending = js.native
  var blendSrc: BlendingSrcFactor | BlendingDstFactor = js.native
  var blendSrcAlpha: Double = js.native
  var clipIntersection: Boolean = js.native
  var clippingPlanes: js.Array[Plane] = js.native
  var clipShadows: Boolean = js.native
  var colorWrite: Boolean = js.native
  var depthFunc: DepthModes = js.native
  var depthTest: Boolean = js.native
  var depthWrite: Boolean = js.native
  var fog: Boolean = js.native
  var lights: Boolean = js.native
  var name: String = js.native
  var opacity: Double = js.native
  var overdraw: Double = js.native
  var polygonOffset: Boolean = js.native
  var polygonOffsetFactor: Double = js.native
  var polygonOffsetUnits: Double = js.native
  var precision: String | String | String | Null = js.native
  var premultipliedAlpha: Boolean = js.native
  var dithering: Boolean = js.native
  var flatShading: Boolean = js.native
  var side: Side = js.native
  var transparent: Boolean = js.native
  var vertexColors: Colors = js.native
  var visible: Boolean = js.native
}