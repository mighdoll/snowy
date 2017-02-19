package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Material")
class Material extends js.Object with EventDispatcher {
  var id: Double                          = js.native
  var uuid: String                        = js.native
  var name: String                        = js.native
  var side: Side                          = js.native
  var opacity: Double                     = js.native
  var transparent: Boolean                = js.native
  var blending: Blending                  = js.native
  var blendSrc: BlendingDstFactor         = js.native
  var blendDst: BlendingSrcFactor         = js.native
  var blendEquation: BlendingEquation     = js.native
  var depthTest: Boolean                  = js.native
  var depthWrite: Boolean                 = js.native
  var polygonOffset: Boolean              = js.native
  var polygonOffsetFactor: Double         = js.native
  var polygonOffsetUnits: Double          = js.native
  var alphaTest: Double                   = js.native
  var overdraw: Double                    = js.native
  var visible: Boolean                    = js.native
  var needsUpdate: Boolean                = js.native
  def setValues(values: Object): Unit     = js.native
  def clone(material: Material): Material = js.native
  def dispose(): Unit                     = js.native
}
