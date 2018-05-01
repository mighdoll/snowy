package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("InstancedInterleavedBuffer")
class InstancedInterleavedBuffer protected () extends InterleavedBuffer {
  def this(array: ArrayLike[Double], stride: Double, meshPerAttribute: Double = js.native) = this()
  var meshPerAttribute: Double = js.native
}