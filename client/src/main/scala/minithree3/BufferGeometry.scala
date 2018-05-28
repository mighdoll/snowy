package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("BufferGeometry")
class BufferGeometry extends EventDispatcher {
  /**
     * This creates a new BufferGeometry. It also sets several properties to an default value.
     */
  /**
     * Unique number of this buffergeometry instance
     */
  var id: Double = js.native
  var uuid: String = js.native
  var name: String = js.native
  var `type`: String = js.native
  var index: BufferAttribute = js.native
  var attributes: BufferAttribute | js.Array[InterleavedBufferAttribute] = js.native
  var morphAttributes: js.Any = js.native
  var groups: js.Array[js.Any] = js.native
  var boundingBox: Box3 = js.native
  var boundingSphere: Sphere = js.native
  var drawRange: js.Any = js.native
  def getIndex(): BufferAttribute = js.native
  def setIndex(index: BufferAttribute | js.Array[Double]): Unit = js.native
  def addAttribute(name: String, attribute: BufferAttribute | InterleavedBufferAttribute): BufferGeometry = js.native
  def getAttribute(name: String): BufferAttribute | InterleavedBufferAttribute = js.native
  def removeAttribute(name: String): BufferGeometry = js.native
  def addGroup(start: Double, count: Double, materialIndex: Double = js.native): Unit = js.native
  def clearGroups(): Unit = js.native
  def setDrawRange(start: Double, count: Double): Unit = js.native
  /**
     * Bakes matrix transform directly into vertex coordinates.
     */
  def applyMatrix(matrix: Matrix4): BufferGeometry = js.native
  def rotateX(angle: Double): BufferGeometry = js.native
  def rotateY(angle: Double): BufferGeometry = js.native
  def rotateZ(angle: Double): BufferGeometry = js.native
  def translate(x: Double, y: Double, z: Double): BufferGeometry = js.native
  def scale(x: Double, y: Double, z: Double): BufferGeometry = js.native
  def lookAt(v: Vector3): Unit = js.native
  def center(): BufferGeometry = js.native
  def setFromObject(`object`: Object3D): BufferGeometry = js.native
  def setFromPoints(points: js.Array[Vector3]): BufferGeometry = js.native
  def updateFromObject(`object`: Object3D): Unit = js.native
  def fromGeometry(geometry: Geometry, settings: js.Any = js.native): BufferGeometry = js.native
  def fromDirectGeometry(geometry: DirectGeometry): BufferGeometry = js.native
  /**
     * Computes bounding box of the geometry, updating Geometry.boundingBox attribute.
     * Bounding boxes aren't computed by default. They need to be explicitly computed, otherwise they are null.
     */
  def computeBoundingBox(): Unit = js.native
  /**
     * Computes bounding sphere of the geometry, updating Geometry.boundingSphere attribute.
     * Bounding spheres aren't' computed by default. They need to be explicitly computed, otherwise they are null.
     */
  def computeBoundingSphere(): Unit = js.native
  /**
     * Computes vertex normals by averaging face normals.
     */
  def computeVertexNormals(): Unit = js.native
  def merge(geometry: BufferGeometry, offset: Double): BufferGeometry = js.native
  def normalizeNormals(): Unit = js.native
  def toNonIndexed(): BufferGeometry = js.native
  def toJSON(): js.Dynamic = js.native
  def copy(source: BufferGeometry): BufferGeometry = js.native
  /**
     * Disposes the object from memory.
     * You need to call this when you want the bufferGeometry removed while the application is running.
     */
  def dispose(): Unit = js.native
  /**
     * @deprecated Use { BufferGeometry#groups .groups} instead.
     */
  var drawcalls: js.Any = js.native
  /**
     * @deprecated Use { BufferGeometry#groups .groups} instead.
     */
  var offsets: js.Any = js.native
  /**
     * @deprecated Use { BufferGeometry#setIndex .setIndex()} instead.
     */
  def addIndex(index: js.Any): Unit = js.native
  /**
     * @deprecated Use { BufferGeometry#addGroup .addGroup()} instead.
     */
  def addDrawCall(start: js.Any, count: js.Any, indexOffset: js.Any = js.native): Unit = js.native
  /**
     * @deprecated Use { BufferGeometry#clearGroups .clearGroups()} instead.
     */
  def clearDrawCalls(): Unit = js.native
  def addAttribute(name: js.Any, array: js.Any, itemSize: js.Any): js.Dynamic = js.native
}

@js.native
@js.annotation.JSGlobal("BufferGeometry")
object BufferGeometry extends js.Object {
  var MaxIndex: Double = js.native
}