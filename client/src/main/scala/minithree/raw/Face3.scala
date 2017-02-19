package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Face3")
class Face3(
      var a: Double,
      var b: Double,
      var c: Double,
      var vertexNormals: js.Array[Vector3] = js.native,
      var vertexColors: js.Array[Color] = js.native,
      var materialIndex: Double = js.native
) extends js.Object {
  var normal: Vector3                  = js.native
  var color: Color                     = js.native
  var vertexTangents: js.Array[Double] = js.native
  override def clone(): Face3          = js.native
}
