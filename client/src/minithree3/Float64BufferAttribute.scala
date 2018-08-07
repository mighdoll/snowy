package minithree3

import scala.scalajs.js
import scala.scalajs.js.|
import scala.scalajs.js.typedarray._

@js.native
@js.annotation.JSGlobal("Float64BufferAttribute")
class Float64BufferAttribute protected () extends BufferAttribute {
  def this(array: Iterable[Double] | js.Array[Double] | ArrayBuffer, itemSize: Double) = this()
}