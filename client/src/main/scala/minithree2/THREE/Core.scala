package minithree2.THREE

import org.scalajs.dom.raw.Event

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, ScalaJSDefined}
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.|

// TODO: The docs and src conflict for a lot of things, check and update based on src
// TODO: If params are defined as properties in docs, make them mutable and set them to js.native

// TODO: Should this be @ScalaJSDefined
@ScalaJSDefined
trait UpdateRangeParameters extends js.Object {
  val offset: Int
  val count: Int
}

@js.native
@JSGlobal("THREE.BufferAttribute")
class BufferAttribute(var array: ArrayBuffer,
                      var itemSize: Int,
                      var normalized: js.UndefOr[Boolean])
    extends js.Object {
  var uuid: String                       = js.native
  var dynamic: Boolean                   = js.native
  var updateRange: UpdateRangeParameters = js.native
  var version: Int                       = js.native
  var needsUpdate: Boolean               = js.native
  var count: Int                         = js.native

  def setDynamic(value: Boolean): BufferAttribute    = js.native
  override def clone(): BufferAttribute              = js.native
  def copy(source: BufferAttribute): BufferAttribute = js.native
  def copyAt(index1: Int,
             bufferAttribute: BufferAttribute,
             index2: Int): BufferAttribute                                 = js.native
  def copyArray(array: ArrayBuffer): BufferAttribute                       = js.native
  def copyColorsArray(colors: js.Array[Color]): BufferAttribute            = js.native
  def copyIndicesArray(indices: js.Array[Face3]): BufferAttribute          = js.native
  def copyVector2sArray(vectors: js.Array[Vector2]): BufferAttribute       = js.native
  def copyVector3sArray(vectors: js.Array[Vector3]): BufferAttribute       = js.native
  def copyVector4sArray(vectors: js.Array[Vector4]): BufferAttribute       = js.native
  def set(value: js.Array[Double], offset: Int): Unit                      = js.native
  def getX(index: Int): Double                                             = js.native
  def setX(index: Int, x: Double): BufferAttribute                         = js.native
  def getY(index: Int): Double                                             = js.native
  def setY(index: Int, y: Double): BufferAttribute                         = js.native
  def getZ(index: Int): Double                                             = js.native
  def setZ(index: Int, z: Double): BufferAttribute                         = js.native
  def getW(index: Int): Double                                             = js.native
  def setW(index: Int, w: Double): BufferAttribute                         = js.native
  def setXY(index: Int, x: Double, y: Double): BufferAttribute             = js.native
  def setXYZ(index: Int, x: Double, y: Double, z: Double): BufferAttribute = js.native
  def setXYZW(index: Int, x: Double, y: Double, z: Double, w: Double): BufferAttribute =
    js.native
}

@ScalaJSDefined
trait DrawRangeParameters extends js.Object {
  val start: Int
  val count: Int
}

@ScalaJSDefined
trait GroupsParameters extends js.Object {
  val start: Int
  val count: Int
  val materialIndex: js.UndefOr[Int]
}

@js.native
@JSGlobal("THREE.BufferGeometry")
class BufferGeometry extends js.Object with EventDispatcher {
  val MaxIndex: Int = js.native

  var id: Int                                                            = js.native
  var uuid: String                                                       = js.native
  var name: String                                                       = js.native
  var `type`: String                                                     = js.native
  var index: BufferAttribute                                             = js.native
  var attributes: BufferAttribute | js.Array[InterleavedBufferAttribute] = js.native
  var morphAttributes: js.Any                                            = js.native
  var groups: js.Array[GroupsParameters]                                 = js.native
  var boundingBox: Box3                                                  = js.native
  var boundingSphere: Sphere                                             = js.native
  var drawRange: DrawRangeParameters                                     = js.native

  def getIndex(): BufferAttribute            = js.native
  def setIndex(index: BufferAttribute): Unit = js.native

  def addAttribute(
        name: String,
        attribute: BufferAttribute | InterleavedBufferAttribute
  ): BufferGeometry = js.native

  def getAttribute(name: String): BufferAttribute    = js.native
  def removeAttribute(name: String): BufferAttribute = js.native

  def addGroup(start: Int, count: Int, materialIndex: Int): Unit = js.native
  def clearGroups(): Unit                                        = js.native

  def setDrawRange(start: Int, count: Int): Unit = js.native

  def applyMatrix(matrix: Matrix4): Unit = js.native

  def rotateX(angle: Float): BufferGeometry                   = js.native
  def rotateY(angle: Float): BufferGeometry                   = js.native
  def rotateZ(angle: Float): BufferGeometry                   = js.native
  def translate(x: Float, y: Float, z: Float): BufferGeometry = js.native
  def scale(x: Float, y: Float, z: Float): BufferGeometry     = js.native
  def lookAt(vector: Vector3): BufferGeometry                 = js.native

  def center(): Vector3 = js.native

  def setFromObject(`object`: Object3D): BufferGeometry    = js.native
  def updateFromObject(`object`: Object3D): BufferGeometry = js.native

  def fromGeometry(Geometry: Geometry): BufferGeometry = js.native

  def fromDirectGeometry(Geometry: Geometry): BufferGeometry = js.native

  def computeBoundingBox(): Unit    = js.native
  def computeBoundingSphere(): Unit = js.native
  def computeVertexNormals(): Unit  = js.native

  def merge(bufferGeometry: BufferGeometry, offset: Int): BufferGeometry = js.native
  def normalizeNormals(): Unit                                           = js.native

  def toNonIndexed(): BufferGeometry = js.native

  def toJSON(): js.Object = js.native

  override def clone(): BufferGeometry                     = js.native
  def copy(bufferGeometry: BufferGeometry): BufferGeometry = js.native

  def dispose(): Unit = js.native
}

@js.native
@JSGlobal("THREE.Clock")
class Clock(var autoStart: js.UndefOr[Boolean] = js.native) extends js.Object {
  val startTime: Float        = js.native
  val oldTime: Float          = js.native
  val elapsedTime: Float      = js.native
  val running: Boolean        = js.native
  def start(): Unit           = js.native
  def stop(): Unit            = js.native
  def getElapsedTime(): Float = js.native
  def getDelta(): Float       = js.native
}

@js.native
@JSGlobal("THREE.DirectGeometry")
class DirectGeometry extends js.Object with EventDispatcher {
  var id: Int                            = js.native
  var uuid: String                       = js.native
  var name: String                       = js.native
  var `type`: String                     = js.native
  var indices: js.Array[Int]             = js.native
  var vertices: js.Array[Vector3]        = js.native
  var normals: js.Array[Vector3]         = js.native
  var colors: js.Array[Color]            = js.native
  var uvs: js.Array[Vector2]             = js.native
  var uvs2: js.Array[Vector2]            = js.native
  var groups: js.Array[GroupsParameters] = js.native
  //var morphTargets: js.Array[MorphTarget] = js.native
  var skinWeights: js.Array[Double] = js.native
  var skinIndices: js.Array[Int]    = js.native
  var boundingBox: Box3             = js.native
  var boundingSphere: Sphere        = js.native
  var verticesNeedUpdate: Boolean   = js.native
  var normalsNeedUpdate: Boolean    = js.native
  var colorsNeedUpdate: Boolean     = js.native
  var uvsNeedUpdate: Boolean        = js.native
  var groupsNeedUpdate: Boolean     = js.native

  def computeBoundingBox(): Unit                       = js.native
  def computeBoundingSphere(): Unit                    = js.native
  def computeGroups(geometry: Geometry): Unit          = js.native
  def fromGeometry(geometry: Geometry): DirectGeometry = js.native
  def dispose(): Unit                                  = js.native
}

@js.native
trait EventDispatcher extends js.Object {
  def addEventListener(`type`: String, listener: js.Function1[Event, _]): Unit =
    js.native
  def hasEventListener(`type`: String, listener: js.Function1[Event, _]): Boolean =
    js.native
  def removeEventListener(`type`: String, listener: js.Function1[Event, _]): Unit =
    js.native
  def dispatchEvent(event: js.Object): Unit = js.native
}

@js.native
@JSGlobal("THREE.Face3")
class Face3(var a: Int,
            var b: Int,
            var c: Int,
            normal: js.UndefOr[Vector3 | js.Array[Vector3]] = js.native,
            color: js.UndefOr[Color | js.Array[Color]] = js.native,
            materialIndex: js.UndefOr[Int] = js.native)
    extends js.Object {
  val vertexNormals: js.Array[Vector3] = js.native
  val vertexColors: js.Array[Color]    = js.native
  override def clone(): Face3          = js.native
  def copy(face3: Face3): Face3        = js.native
}

@ScalaJSDefined
trait MorphTargetParameters extends js.Object {
  val name: String
  val vertices: js.Array[Vector3]
}

@ScalaJSDefined
trait MorphNormalParameters extends js.Object {
  val name: String
  val normals: js.Array[Vector3]
}

@js.native
@JSGlobal("THREE.Geometry")
class Geometry extends js.Object with EventDispatcher {
  val boundingBox: Box3                                      = js.native
  val boundingSphere: Sphere                                 = js.native
  val colors: js.Array[Color]                                = js.native
  val faces: js.Array[Face3]                                 = js.native
  val lineDistances: js.Array[Double]                        = js.native
  val morphTargets: js.Array[MorphTargetParameters]          = js.native
  val morphNormals: js.Array[MorphNormalParameters]          = js.native
  val name: String                                           = js.native
  val skinWeights: js.Array[Vector4]                         = js.native
  val skinIndices: js.Array[Vector4]                         = js.native
  val uuid: String                                           = js.native
  val vertices: js.Array[Vector3]                            = js.native
  val verticesNeedUpdate: Boolean                            = js.native
  val elementsNeedUpdate: Boolean                            = js.native
  val uvsNeedUpdate: Boolean                                 = js.native
  val normalsNeedUpdate: Boolean                             = js.native
  val colorsNeedUpdate: Boolean                              = js.native
  val groupsNeedUpdate: Boolean                              = js.native
  val lineDistancesNeedUpdate: Boolean                       = js.native
  def applyMatrix(matrix: Matrix4): Unit                     = js.native
  def center(): Unit                                         = js.native
  override def clone(): Geometry                             = js.native
  def computeBoundingBox(): Unit                             = js.native
  def computeBoundingSphere(): Unit                          = js.native
  def computeFaceNormals(): Unit                             = js.native
  def computeFlatVertexNormals(): Unit                       = js.native
  def computeLineDistances(): Unit                           = js.native
  def computeMorphNormals(): Unit                            = js.native
  def computeVertexNormals(areaWeighted: Boolean): Unit      = js.native
  def copy(geometry: Geometry): Geometry                     = js.native
  def dispose(): Unit                                        = js.native
  def fromBufferGeometry(geometry: BufferGeometry): Geometry = js.native
  def lookAt(vector: Vector3): Geometry                      = js.native
  def merge(geometry: Geometry, matrix: Matrix4, materialIndexOffset: Int): Unit =
    js.native
  //def mergeMesh(mesh: Mesh): Unit                       = js.native
  def mergeVertices(): Unit                             = js.native
  def normalize(): Unit                                 = js.native
  def rotateX(radians: Float): Geometry                 = js.native
  def rotateY(radians: Float): Geometry                 = js.native
  def rotateZ(radians: Float): Geometry                 = js.native
  def sortFacesByMaterialIndex(): Unit                  = js.native
  def scale(x: Float, y: Float, z: Float): Geometry     = js.native
  def toJSON(): js.Object                               = js.native
  def translate(x: Float, y: Float, z: Float): Geometry = js.native
}

@js.native
@JSGlobal("THREE.InstancedBufferAttribute")
class InstancedBufferAttribute(array: ArrayBuffer,
                               itemSize: Int,
                               normalized: js.UndefOr[Boolean] = js.native)
    extends BufferAttribute(array, itemSize, normalized) {
  val meshPerAttribute: Int               = js.native
  val isInstancedBufferAttribute: Boolean = js.native
}

@js.native
@JSGlobal("THREE.InstancedBufferGeometry")
class InstancedBufferGeometry extends BufferGeometry {
  val meshPerAttribute: Int              = js.native
  val isInstancedBufferGeometry: Boolean = js.native
}

@js.native
@JSGlobal("THREE.InstancedInterleavedBuffer")
class InstancedInterleavedBuffer(array: ArrayBuffer,
                                 itemSize: Int,
                                 var meshPerAttribute: Int = js.native)
    extends InterleavedBuffer(array, itemSize) {
  val isInstancedInterleavedBuffer: Boolean = js.native
}

@js.native
@JSGlobal("THREE.InterleavedBuffer")
class InterleavedBuffer(array: ArrayBuffer, stride: Int) extends js.Object {
  var count: Int                                         = js.native
  var dynamic: Boolean                                   = js.native
  var updateRange: UpdateRangeParameters                 = js.native
  var version: Int                                       = js.native
  val isInterleavedBuffer: Boolean                       = js.native
  var needsUpdate: Boolean                               = js.native
  def setArray(array: ArrayBuffer): Unit                 = js.native
  def setDynamic(value: Boolean): InterleavedBuffer      = js.native
  def copy(source: InterleavedBuffer): InterleavedBuffer = js.native
  def copyAt(index1: Int, attribute: BufferAttribute, index2: Int): InterleavedBuffer =
    js.native
  def set(value: js.Any, offset: Int): InterleavedBuffer = js.native
  override def clone(): InterleavedBuffer                = js.native
}

@js.native
@JSGlobal("THREE.InterleavedBufferAttribute")
class InterleavedBufferAttribute(interleavedBuffer: InterleavedBuffer,
                                 itemSize: Int,
                                 offset: Int,
                                 normalized: Boolean)
    extends js.Object {
  var data: InterleavedBuffer                                             = js.native
  var isInterleavedBufferAttribute: Boolean                               = js.native
  def count(): Int                                                        = js.native
  def array(): ArrayBuffer                                                = js.native
  def getX(index: Int): Double                                            = js.native
  def getY(index: Int): Double                                            = js.native
  def getZ(index: Int): Double                                            = js.native
  def getW(index: Int): Double                                            = js.native
  def setX(index: Int, x: Double): InterleavedBufferAttribute             = js.native
  def setY(index: Int, y: Double): InterleavedBufferAttribute             = js.native
  def setZ(index: Int, z: Double): InterleavedBufferAttribute             = js.native
  def setXY(index: Int, x: Double, y: Double): InterleavedBufferAttribute = js.native
  def setXYZ(index: Int, x: Double, y: Double, z: Double): InterleavedBufferAttribute =
    js.native
  def setXYZW(index: Int,
              x: Double,
              y: Double,
              z: Double,
              w: Double): InterleavedBufferAttribute = js.native
}

@js.native
@JSGlobal("THREE.Layers")
class Layers extends js.Object {
  var mask: Int                 = js.native
  def disable(layer: Int): Unit = js.native
  def enable(layer: Int): Unit  = js.native
  def set(layer: Int): Unit     = js.native
  def test(layers: Int): Unit   = js.native
  def toggle(layer: Int): Unit  = js.native
}

@js.native
@JSGlobal("THREE.Object3D")
class Object3D extends EventDispatcher {
  var castShadow: Boolean              = js.native
  var children: js.Array[Object3D]     = js.native
  var frustumCulled: Boolean           = js.native
  var id: Int                          = js.native
  var isObject: Boolean                = js.native
  var layers: Layers                   = js.native
  var matrix: Matrix4                  = js.native
  var matrixAutoUpdate: Boolean        = js.native
  var matrixWorld: Matrix4             = js.native
  var matrixWorldNeedsUpdate: Boolean  = js.native
  var modelViewMatrix: Matrix4         = js.native
  var name: String                     = js.native
  var normalMatrix: Matrix3            = js.native
  var onAfterRender: js.Function       = js.native
  var onBeforeRender: js.Function      = js.native
  var parent: Object3D                 = js.native
  var position: Vector3                = js.native
  var quaternion: Quaternion           = js.native
  var receiveShadow: Boolean           = js.native
  var renderOrder: Number              = js.native
  var rotation: Euler                  = js.native
  var scale: Vector3                   = js.native
  var up: Vector3                      = js.native
  var userData: js.Object              = js.native
  var uuid: String                     = js.native
  var visible: Boolean                 = js.native
  var DefaultUp: Vector3               = js.native
  var DefaultMatrixAutoUpdate: Vector3 = js.native

  def add(`object`: Object3D*): Unit                              = js.native
  def applyMatrix(matrix: Matrix4): Unit                          = js.native
  def clone(recursive: Boolean): Object3D                         = js.native
  def copy(`object`: Object3D, recursive: Boolean): Object3D      = js.native
  def getObjectById(id: Int): Object3D                            = js.native
  def getObjectByName(name: String): Object3D                     = js.native
  def getObjectByProperty(name: String, value: Float): Object3D   = js.native
  def getWorldPosition(optionalTarget: Vector3): Vector3          = js.native
  def getWorldQuaternion(optionalTarget: Quaternion): Quaternion  = js.native
  def getWorldRotation(optionalTarget: Euler): Euler              = js.native
  def getWorldScale(optionalTarget: Vector3): Vector3             = js.native
  def getWorldDirection(optionalTarget: Vector3): Vector3         = js.native
  def localToWorld(vector: Vector3): Vector3                      = js.native
  def lookAt(vector: Vector3): Unit                               = js.native
  def raycast(): Unit                                             = js.native
  def remove(`object`: Object3D*): Unit                           = js.native
  def rotateOnAxis(axis: Vector3, angle: Float): Object3D         = js.native
  def rotateX(rad: Float): Unit                                   = js.native
  def rotateY(rad: Float): Unit                                   = js.native
  def rotateZ(rad: Float): Unit                                   = js.native
  def setRotationFromAxisAngle(axis: Vector3, angle: Float): Unit = js.native
  def setRotationFromEuler(euler: Euler): Unit                    = js.native
  def setRotationFromMatrix(m: Matrix4): Unit                     = js.native
  def setRotationFromQuaternion(q: Quaternion): Unit              = js.native
  def toJSON(meta: js.Object): Unit                               = js.native
  def translateOnAxis(axis: Vector3, distance: Float): Object3D   = js.native
  def translateX(distance: Float): Unit                           = js.native
  def translateY(distance: Float): Unit                           = js.native
  def translateZ(distance: Float): Unit                           = js.native
  def traverse(callback: js.Function): Unit                       = js.native
  def traverseVisible(callback: js.Function): Unit                = js.native
  def traverseAncestors(callback: js.Function): Unit              = js.native
  def updateMatrix(): Unit                                        = js.native
  def updateMatrixWorld(force: Boolean): Unit                     = js.native
  def worldToLocal(vector: Vector3): Vector3                      = js.native
}

// TODO: Are (js.Object)s the same as (Dynamic.literal)s
@ScalaJSDefined
trait RaycasterParameters extends js.Object {
  val Mesh: js.Object
  val Line: js.Object
  val LOD: js.Object
  val Points: js.Object
  val Sprite: js.Object
}

@ScalaJSDefined
trait Intersection extends js.Object {
  val distance: Double
  val distanceToRay: Double
  val point: Vector3
  val index: Int
  val face: Face3
  val faceIndex: Int
  val `object`: Object3D
}

@js.native
@JSGlobal("THREE.Raycaster")
class Raycaster(origin: js.UndefOr[Vector3],
                direction: js.UndefOr[Vector3],
                var near: js.UndefOr[Float] = js.native,
                var far: js.UndefOr[Float] = js.native)
    extends js.Object {
  var linePrecision: Float                           = js.native
  var params: RaycasterParameters                    = js.native
  var ray: Ray                                       = js.native
  def set(origin: Vector3, direction: Vector3): Unit = js.native
  //def setFromCamera(coords: Vector2, camera: Camera): Unit = js.native
  def intersectObject(`object`: Object3D,
                      recursive: js.UndefOr[Boolean]): js.Array[Intersection] = js.native
  def intersectObjects(objects: js.Array[Object3D],
                       recursive: js.UndefOr[Boolean]): js.Array[Intersection] =
    js.native

}

@js.native
@JSGlobal("THREE.Uniform")
class Uniform extends js.Object {}
