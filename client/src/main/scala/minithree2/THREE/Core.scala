package minithree2.THREE

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, ScalaJSDefined}
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.|

@ScalaJSDefined
trait UpdateRangeParameters extends js.Object {
  val offset: js.UndefOr[Int] = js.undefined
  val count: js.UndefOr[Int]  = js.undefined
}

@js.native
@JSGlobal("THREE.BufferAttribute")
class BufferAttribute(var array: ArrayBuffer,
                      var itemSize: Int,
                      var normalized: js.UndefOr[Boolean])
    extends js.Object {
  var count: Int                                                        = js.native
  var dynamic: Boolean                                                  = js.native
  var isBufferAttribute: Boolean                                        = js.native
  var needsUpdate: Boolean                                              = js.native
  var onUploadCallback: js.Function                                     = js.native
  var updateRange: UpdateRangeParameters                                = js.native
  var uuid: String                                                      = js.native
  var version: Int                                                      = js.native
  override def clone(): BufferAttribute                                 = js.native
  def copyArray(array: js.Array[Double] | ArrayBuffer): BufferAttribute = js.native
  def copyAt(index1: Int, bufferAttribute: BufferAttribute, index2: Int): Unit =
    js.native
  //def copyColorsArray(colors: js.Array[Color]): BufferAttribute = js.native

}
