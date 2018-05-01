package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("TubeBufferGeometry")
class TubeBufferGeometry protected () extends BufferGeometry {
  def this(path: Curve[Vector3], segments: Double = js.native, radius: Double = js.native, radiusSegments: Double = js.native, closed: Boolean = js.native) = this()
  var parameters: js.Any = js.native
  var tangents: js.Array[Vector3] = js.native
  var normals: js.Array[Vector3] = js.native
  var binormals: js.Array[Vector3] = js.native
}