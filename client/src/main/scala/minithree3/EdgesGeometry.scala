package minithree3

import scala.scalajs.js
import com.definitelyscala.node.Buffer

@js.native
@js.annotation.JSGlobal("EdgesGeometry")
class EdgesGeometry protected () extends BufferGeometry {
  def this(geometry: BufferGeometry, thresholdAngle: Double) = this()
}