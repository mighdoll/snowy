package minithree3

import scala.scalajs.js
import scala.scalajs.js.typedarray._

@js.native
@js.annotation.JSGlobal("InterleavedBuffer")
class InterleavedBuffer protected () extends js.Object {
  def this(array: js.Array[Double], stride: Double) = this()
  var array: js.Array[Double] = js.native
  var stride: Double = js.native
  var dynamic: Boolean = js.native
  var updateRange: js.Any = js.native
  var version: Double = js.native
  var length: Double = js.native
  var count: Double = js.native
  var needsUpdate: Boolean = js.native
  def setArray(array: ArrayBufferView = js.native): Unit = js.native
  def setDynamic(dynamic: Boolean): InterleavedBuffer = js.native
  def copy(source: InterleavedBuffer): InterleavedBuffer = js.native
  def copyAt(index1: Double, attribute: InterleavedBufferAttribute, index2: Double): InterleavedBuffer = js.native
  def set(value: js.Array[Double], index: Double): InterleavedBuffer = js.native
}