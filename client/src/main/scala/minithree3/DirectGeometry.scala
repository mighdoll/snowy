package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
@js.annotation.JSGlobal("DirectGeometry")
class DirectGeometry extends EventDispatcher {
  var id: Double = js.native
  var uuid: String = js.native
  var name: String = js.native
  var `type`: String = js.native
  var indices: js.Array[Double] = js.native
  var vertices: js.Array[Vector3] = js.native
  var normals: js.Array[Vector3] = js.native
  var colors: js.Array[Color] = js.native
  var uvs: js.Array[Vector2] = js.native
  var uvs2: js.Array[Vector2] = js.native
  var groups: js.Array[js.Any] = js.native
  var morphTargets: js.Array[MorphTarget] = js.native
  var skinWeights: js.Array[Vector4] = js.native
  var skinIndices: js.Array[Vector4] = js.native
  var boundingBox: Box3 = js.native
  var boundingSphere: Sphere = js.native
  var verticesNeedUpdate: Boolean = js.native
  var normalsNeedUpdate: Boolean = js.native
  var colorsNeedUpdate: Boolean = js.native
  var uvsNeedUpdate: Boolean = js.native
  var groupsNeedUpdate: Boolean = js.native
  def computeBoundingBox(): Unit = js.native
  def computeBoundingSphere(): Unit = js.native
  def computeGroups(geometry: Geometry): Unit = js.native
  def fromGeometry(geometry: Geometry): DirectGeometry = js.native
  def dispose(): Unit = js.native
  // EventDispatcher mixins
  def addEventListener(`type`: String, listener: js.Function1[Event, Unit]): Unit = js.native
  def hasEventListener(`type`: String, listener: js.Function1[Event, Unit]): Unit = js.native
  def removeEventListener(`type`: String, listener: js.Function1[Event, Unit]): Unit = js.native
  def dispatchEvent(event: js.Any): Unit = js.native
}