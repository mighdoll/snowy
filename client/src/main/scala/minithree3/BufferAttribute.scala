package minithree3

import scala.scalajs.js
import com.definitelyscala.node.Buffer
import scala.scalajs.js.|
import scala.scalajs.js.typedarray._

@js.native
@js.annotation.JSGlobal("BufferAttribute")
class BufferAttribute protected () extends js.Object {
  def this(array: ArrayLike[Double], itemSize: Double, normalized: Boolean = js.native) = this()
  // array parameter should be TypedArray.
  var uuid: String = js.native
  var array: ArrayLike[Double] = js.native
  var itemSize: Double = js.native
  var dynamic: Boolean = js.native
  var updateRange: js.Any = js.native
  var version: Double = js.native
  var normalized: Boolean = js.native
  var needsUpdate: Boolean = js.native
  var count: Double = js.native
  var onUpload: js.Function = js.native
  def setArray(array: ArrayBufferView = js.native): Unit = js.native
  def setDynamic(dynamic: Boolean): BufferAttribute = js.native
  def copy(source: BufferAttribute): BufferAttribute = js.native
  def copyAt(index1: Double, attribute: BufferAttribute, index2: Double): BufferAttribute = js.native
  def copyArray(array: ArrayLike[Double]): BufferAttribute = js.native
  def copyColorsArray(colors: js.Array[js.Any]): BufferAttribute = js.native
  def copyVector2sArray(vectors: js.Array[js.Any]): BufferAttribute = js.native
  def copyVector3sArray(vectors: js.Array[js.Any]): BufferAttribute = js.native
  def copyVector4sArray(vectors: js.Array[js.Any]): BufferAttribute = js.native
  def set(value: ArrayLike[Double] | ArrayBufferView, offset: Double = js.native): BufferAttribute = js.native
  def getX(index: Double): Double = js.native
  def setX(index: Double, x: Double): BufferAttribute = js.native
  def getY(index: Double): Double = js.native
  def setY(index: Double, y: Double): BufferAttribute = js.native
  def getZ(index: Double): Double = js.native
  def setZ(index: Double, z: Double): BufferAttribute = js.native
  def getW(index: Double): Double = js.native
  def setW(index: Double, z: Double): BufferAttribute = js.native
  def setXY(index: Double, x: Double, y: Double): BufferAttribute = js.native
  def setXYZ(index: Double, x: Double, y: Double, z: Double): BufferAttribute = js.native
  def setXYZW(index: Double, x: Double, y: Double, z: Double, w: Double): BufferAttribute = js.native
  /**
     * @deprecated Use { BufferAttribute#count .count} instead.
     */
  var length: Double = js.native
}