package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("TubeGeometry")
class TubeGeometry protected () extends Geometry {
  def this(path: Curve[Vector3], segments: Double = js.native, radius: Double = js.native, radiusSegments: Double = js.native, closed: Boolean = js.native, taper: js.Function1[Double, Double] = js.native) = this()
  var parameters: js.Any = js.native
  var tangents: js.Array[Vector3] = js.native
  var normals: js.Array[Vector3] = js.native
  var binormals: js.Array[Vector3] = js.native
}

@js.native
@js.annotation.JSGlobal("TubeGeometry")
object TubeGeometry extends js.Object {
  def NoTaper(u: Double = js.native): Double = js.native
  def SinusoidalTaper(u: Double): Double = js.native
  def FrenetFrames(path: Path, segments: Double, closed: Boolean): Unit = js.native
}