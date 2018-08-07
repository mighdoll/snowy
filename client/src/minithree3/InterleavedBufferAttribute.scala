package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("InterleavedBufferAttribute")
class InterleavedBufferAttribute protected () extends js.Object {
  def this(interleavedBuffer: InterleavedBuffer, itemSize: Double, offset: Double, normalized: Boolean = js.native) = this()
  var uuid: String = js.native
  var data: InterleavedBuffer = js.native
  var itemSize: Double = js.native
  var offset: Double = js.native
  var count: Double = js.native
  var normalized: Boolean = js.native
  var array: js.Array[js.Any] = js.native
  def getX(index: Double): Double = js.native
  def setX(index: Double, x: Double): InterleavedBufferAttribute = js.native
  def getY(index: Double): Double = js.native
  def setY(index: Double, y: Double): InterleavedBufferAttribute = js.native
  def getZ(index: Double): Double = js.native
  def setZ(index: Double, z: Double): InterleavedBufferAttribute = js.native
  def getW(index: Double): Double = js.native
  def setW(index: Double, z: Double): InterleavedBufferAttribute = js.native
  def setXY(index: Double, x: Double, y: Double): InterleavedBufferAttribute = js.native
  def setXYZ(index: Double, x: Double, y: Double, z: Double): InterleavedBufferAttribute = js.native
  def setXYZW(index: Double, x: Double, y: Double, z: Double, w: Double): InterleavedBufferAttribute = js.native
  /**
     * @deprecated Use { InterleavedBufferAttribute#count .count} instead.
     */
  var length: Double = js.native
}