package minithree2.THREE

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, ScalaJSDefined}
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.|

@ScalaJSDefined
trait UpdateRangeParameters extends js.Object {
  val offset: js.UndefOr[Int] = js.undefined
  val count: js.UndefOr[Int]  = js.undefined
}

@js.native
@JSGlobal("THREE.BufferAttribute")
class BufferAttribute(var array: ArrayBuffer,
                      var itemSize: Int,
                      var normalized: js.UndefOr[Boolean])
    extends js.Object {
  var count: Int                                                        = js.native
  var dynamic: Boolean                                                  = js.native
  var isBufferAttribute: Boolean                                        = js.native
  var needsUpdate: Boolean                                              = js.native
  var onUploadCallback: js.Function                                     = js.native
  var updateRange: UpdateRangeParameters                                = js.native
  var uuid: String                                                      = js.native
  var version: Int                                                      = js.native
  override def clone(): BufferAttribute                                 = js.native
  def copyArray(array: js.Array[Double] | ArrayBuffer): BufferAttribute = js.native
  def copyAt(index1: Int, bufferAttribute: BufferAttribute, index2: Int): Unit =
    js.native
  def copyColorsArray(colors: js.Array[Color]): BufferAttribute = js.native
  //def copyIndicesArray(indices: js.Array[Face3]): BufferAttribute    = js.native
  def copyVector2sArray(vectors: js.Array[Vector2]): BufferAttribute        = js.native
  def copyVector3sArray(vectors: js.Array[Vector3]): BufferAttribute        = js.native
  def copyVector4sArray(vectors: js.Array[Vector4]): BufferAttribute        = js.native
  def getX(index: Int): Double                                              = js.native
  def getY(index: Int): Double                                              = js.native
  def getZ(index: Int): Double                                              = js.native
  def getW(index: Int): Double                                              = js.native
  def onUpload(callback: js.Function): Unit                                 = js.native
  def set(value: js.Array[Double], offset: Int): Unit                       = js.native
  def setArray(array: ArrayBuffer): Unit                                    = js.native
  def setDynamic(value: Boolean): BufferAttribute                           = js.native
  def setX(index: Int, x: Double): Unit                                     = js.native
  def setY(index: Int, y: Double): Unit                                     = js.native
  def setZ(index: Int, z: Double): Unit                                     = js.native
  def setW(index: Int, w: Double): Unit                                     = js.native
  def setXY(index: Int, x: Double, y: Double): Unit                         = js.native
  def setXYZ(index: Int, x: Double, y: Double, z: Double): Unit             = js.native
  def setXYZW(index: Int, x: Double, y: Double, z: Double, w: Double): Unit = js.native
}

@ScalaJSDefined
trait DrawRangeParameters extends js.Object {
  val start: js.UndefOr[Int] = js.undefined
  val count: js.UndefOr[Int] = js.undefined
}

@ScalaJSDefined
trait GroupsParameters extends js.Object {
  val start: js.UndefOr[Int]         = js.undefined
  val count: js.UndefOr[Int]         = js.undefined
  val materialIndex: js.UndefOr[Int] = js.undefined
}

@js.native
@JSGlobal("THREE.BufferGeometry")
class BufferGeometry extends js.Object with EventDispatcher {
  var attributes: js.Object                                        = js.native
  var boundingBox: Box3                                            = js.native
  var boundingSphere: Sphere                                       = js.native
  var drawRange: DrawRangeParameters                               = js.native
  var groups: GroupsParameters                                     = js.native
  var id: Int                                                      = js.native
  var index: BufferAttribute                                       = js.native
  var isBufferGeometry: Boolean                                    = js.native
  var MaxIndex: Int                                                = js.native
  var morphAttributes: js.Object                                   = js.native
  var name: String                                                 = js.native
  var uuid: String                                                 = js.native
  def addAttribute(name: String, attribute: BufferAttribute): Unit = js.native
  def addGroup(start: Int, count: Int, materialIndex: Int): Unit   = js.native
  def applyMatrix(matrix: Matrix4): Unit                           = js.native
  def center(): Unit                                               = js.native
  override def clone(): BufferGeometry                             = js.native
  def copy(bufferGeometry: BufferGeometry): BufferGeometry         = js.native
  def clearGroups(): Unit                                          = js.native
  def computeBoundingBox(): Unit                                   = js.native
  def computeBoundingSphere(): Unit                                = js.native
  def computeVertexNormals(): Unit                                 = js.native
  def dispose(): Unit                                              = js.native
  def fromDirectGeometry(Geometry: Geometry): BufferGeometry       = js.native
  def fromGeometry(Geometry: Geometry): BufferGeometry             = js.native
  def getAttribute(name: String): BufferAttribute                  = js.native
  def getIndex(): BufferAttribute                                  = js.native
  def lookAt(vector: Vector3): BufferGeometry                      = js.native
  def merge(bufferGeometry: BufferGeometry, offset: Int): Unit     = js.native
  def normalizeNormals(): Unit                                     = js.native
  def removeAttribute(name: String): BufferAttribute               = js.native
  def rotateX(radians: Float): BufferGeometry                      = js.native
  def rotateY(radians: Float): BufferGeometry                      = js.native
  def rotateZ(radians: Float): BufferGeometry                      = js.native
  def scale(x: Float, y: Float, z: Float): BufferGeometry          = js.native
  def setIndex(index: BufferAttribute): Unit                       = js.native
  def setDrawRange(start: Int, count: Int): Unit                   = js.native
  def setFromObject(`object`: Object3D): BufferGeometry            = js.native
  def toJSON(): js.Object                                          = js.native
  def toNonIndexed(): BufferGeometry                               = js.native
  def translate(x: Float, y: Float, z: Float): BufferGeometry      = js.native
  def updateFromObject(`object`: Object3D): BufferGeometry         = js.native
}

@js.native
@JSGlobal("THREE.Clock")
class Clock extends js.Object {}

@js.native
@JSGlobal("THREE.DirectGeometry")
class DirectGeometry extends js.Object {}

@js.native
trait EventDispatcher extends js.Object {}

@js.native
@JSGlobal("THREE.Face3")
class Face3 extends js.Object {}

@js.native
@JSGlobal("THREE.Geometry")
class Geometry extends js.Object {}

@js.native
@JSGlobal("THREE.InstancedBufferAttribute")
class InstancedBufferAttribute extends js.Object {}

@js.native
@JSGlobal("THREE.InstancedBufferGeometry")
class InstancedBufferGeometry extends js.Object {}

@js.native
@JSGlobal("THREE.InstancedInterleavedBuffer")
class InstancedInterleavedBuffer extends js.Object {}

@js.native
@JSGlobal("THREE.InterleavedBuffer")
class InterleavedBuffer extends js.Object {}

@js.native
@JSGlobal("THREE.InterleavedBufferAttribute")
class InterleavedBufferAttribute extends js.Object {}

@js.native
@JSGlobal("THREE.Layers")
class Layers extends js.Object {}

@js.native
@JSGlobal("THREE.Object3D")
class Object3D extends js.Object {}

@js.native
@JSGlobal("THREE.Raycaster")
class Raycaster extends js.Object {}

@js.native
@JSGlobal("THREE.Uniform")
class Uniform extends js.Object {}
