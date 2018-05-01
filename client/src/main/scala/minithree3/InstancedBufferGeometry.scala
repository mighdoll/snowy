package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("InstancedBufferGeometry")
class InstancedBufferGeometry extends BufferGeometry {
  var groups: js.Array[js.Any] = js.native
  var maxInstancedCount: Double = js.native
  def addGroup(start: Double, count: Double, instances: Double): Unit = js.native
}