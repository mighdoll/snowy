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
  def copyColorsArray(colors: js.Array[Color]): BufferAttribute = js.native
  //def copyIndicesArray(indices: js.Array[Face3]): BufferAttribute    = js.native
  def copyVector2sArray(vectors: js.Array[Vector2]): BufferAttribute        = js.native
  def copyVector3sArray(vectors: js.Array[Vector3]): BufferAttribute        = js.native
  def copyVector4sArray(vectors: js.Array[Vector4]): BufferAttribute        = js.native
  def getX(index: Int): Double                                              = js.native
  def getY(index: Int): Double                                              = js.native
  def getZ(index: Int): Double                                              = js.native
  def getW(index: Int): Double                                              = js.native
  def onUpload(callback: js.Function): Unit                                 = js.native
  def set(value: js.Array[Double], offset: Int): Unit                       = js.native
  def setArray(array: ArrayBuffer): Unit                                    = js.native
  def setDynamic(value: Boolean): BufferAttribute                           = js.native
  def setX(index: Int, x: Double): Unit                                     = js.native
  def setY(index: Int, y: Double): Unit                                     = js.native
  def setZ(index: Int, z: Double): Unit                                     = js.native
  def setW(index: Int, w: Double): Unit                                     = js.native
  def setXY(index: Int, x: Double, y: Double): Unit                         = js.native
  def setXYZ(index: Int, x: Double, y: Double, z: Double): Unit             = js.native
  def setXYZW(index: Int, x: Double, y: Double, z: Double, w: Double): Unit = js.native
}
