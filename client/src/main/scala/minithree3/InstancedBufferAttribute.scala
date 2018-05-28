package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("InstancedBufferAttribute")
class InstancedBufferAttribute protected () extends BufferAttribute {
  def this(data: js.Array[Double], itemSize: Double, meshPerAttribute: Double = js.native) = this()
  var meshPerAttribute: Double = js.native
}