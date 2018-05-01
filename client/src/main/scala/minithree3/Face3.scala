package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Face3")
class Face3 protected () extends js.Object {
  def this(a: Double, b: Double, c: Double, normal: Vector3 = js.native, color: Color = js.native, materialIndex: Double = js.native) = this()
  def this(a: Double, b: Double, c: Double, normal: Vector3 = js.native, vertexColors: js.Array[Color] = js.native, materialIndex: Double = js.native) = this()
  def this(a: Double, b: Double, c: Double, vertexNormals: js.Array[Vector3] = js.native, color: Color = js.native, materialIndex: Double = js.native) = this()
  def this(a: Double, b: Double, c: Double, vertexNormals: js.Array[Vector3] = js.native, vertexColors: js.Array[Color] = js.native, materialIndex: Double = js.native) = this()
  /**
     * @param a Vertex A index.
     * @param b Vertex B index.
     * @param c Vertex C index.
     * @param normal Face normal or array of vertex normals.
     * @param color Face color or array of vertex colors.
     * @param materialIndex Material index.
     */
  /**
     * Vertex A index.
     */
  var a: Double = js.native
  /**
     * Vertex B index.
     */
  var b: Double = js.native
  /**
     * Vertex C index.
     */
  var c: Double = js.native
  /**
     * Face normal.
     */
  var normal: Vector3 = js.native
  /**
     * Array of 4 vertex normals.
     */
  var vertexNormals: js.Array[Vector3] = js.native
  /**
     * Face color.
     */
  var color: Color = js.native
  /**
     * Array of 4 vertex normals.
     */
  var vertexColors: js.Array[Color] = js.native
  /**
     * Material index (points to { Geometry.materials}).
     */
  var materialIndex: Double = js.native
  def copy(source: Face3): Face3 = js.native
}