package minithree.THREE

import org.scalajs.dom.{HTMLCanvasElement, HTMLElement}

import scala.scalajs.js
import scala.scalajs.js.annotation.*

@js.native
@JSImport("stats.js", JSImport.Default)
class Stats extends js.Object {
  var dom: HTMLElement        = js.native
  def showPanel(i: Int): Unit = js.native
  def begin(): js.Any         = js.native
  def end(): js.Any           = js.native
}

@js.native
@JSImport("three", JSImport.Namespace)
object jsTHREE extends js.Object {
  val DoubleSide: Int     = js.native
  val RepeatWrapping: Int = js.native
}

@js.native
@JSImport("three", "Vector2")
class Vector2 extends js.Object {
  def this(x: Double, y: Double) = this()
  var x: Double                          = js.native
  var y: Double                          = js.native
  def set(x: Double, y: Double): Vector2 = js.native
}

@js.native
@JSImport("three", "Vector3")
class Vector3 extends js.Object {
  def this(x: Double, y: Double, z: Double) = this()
  var x: Double                                     = js.native
  var y: Double                                     = js.native
  var z: Double                                     = js.native
  def set(x: Double, y: Double, z: Double): Vector3 = js.native
  override def clone(): Vector3                     = js.native
  def add(v: Vector3): Vector3                      = js.native
  def multiplyScalar(s: Double): Vector3            = js.native
}

@js.native
@JSImport("three", "Euler")
class Euler extends js.Object {
  var x: Double = js.native
  var y: Double = js.native
  var z: Double = js.native
}

@js.native
@JSImport("three", "Color")
class Color(hex: Int = js.native) extends js.Object {
  def setHex(hex: Int): Color = js.native
}

@js.native
@JSImport("three", "Object3D")
class Object3D extends js.Object {
  val position: Vector3                = js.native
  val rotation: Euler                  = js.native
  val scale: Vector3                   = js.native
  var name: String                     = js.native
  var visible: Boolean                 = js.native
  var children: js.Array[Object3D]     = js.native
  def add(obj: Object3D*): Object3D    = js.native
  def remove(obj: Object3D*): Object3D = js.native
}

@js.native
@JSImport("three", "Scene")
class Scene extends Object3D

@js.native
@JSImport("three", "Camera")
class Camera extends Object3D

@js.native
@JSImport("three", "PerspectiveCamera")
class PerspectiveCamera(fov: Double, aspectRatio: Double, near: Double, far: Double)
    extends Camera {
  var aspect: Double                 = js.native
  def updateProjectionMatrix(): Unit = js.native
  def lookAt(v: Vector3): Unit       = js.native
}

@js.native
@JSImport("three", "AmbientLight")
class AmbientLight(color: Int = js.native, intensity: Double = js.native) extends Object3D

@js.native
@JSImport("three", "DirectionalLight")
class DirectionalLight(color: Int = js.native, intensity: Double = js.native)
    extends Object3D

@js.native
@JSImport("three", "BufferAttribute")
class BufferAttribute extends js.Object {
  val count: Int                                   = js.native
  var needsUpdate: Boolean                         = js.native
  def setZ(index: Int, z: Double): BufferAttribute = js.native
}

@js.native
@JSImport("three", "BufferGeometry")
class BufferGeometry extends js.Object {
  def getAttribute(name: String): BufferAttribute = js.native
  def computeVertexNormals(): Unit                = js.native
}

@js.native
@JSImport("three", "BoxGeometry")
class BoxGeometry(width: Double, height: Double, depth: Double) extends BufferGeometry

@js.native
@JSImport("three", "PlaneGeometry")
class PlaneGeometry(
      width: Double,
      height: Double,
      widthSegments: Int = js.native,
      heightSegments: Int = js.native
) extends BufferGeometry

@js.native
@JSImport("three", "ConeGeometry")
class ConeGeometry(
      radius: Double,
      height: Double,
      radialSegments: Int,
      heightSegments: Int,
      openEnded: Boolean,
      thetaStart: Double,
      thetaLength: Double
) extends BufferGeometry

@js.native
trait ExtrudeGeometryOptions extends js.Object

@js.native
@JSImport("three", "ExtrudeGeometry")
class ExtrudeGeometry(shape: Shape, options: ExtrudeGeometryOptions)
    extends BufferGeometry

@js.native
@JSImport("three", "Shape")
class Shape extends js.Object {
  val holes: js.Array[Shape]             = js.native
  def moveTo(x: Double, y: Double): Unit = js.native
  def lineTo(x: Double, y: Double): Unit = js.native
}

@js.native
trait Material extends js.Object {
  var transparent: Boolean        = js.native
  var opacity: Double             = js.native
  override def clone(): this.type = js.native
}

@js.native
trait MeshLambertMaterialParameters extends js.Object

@js.native
@JSImport("three", "MeshLambertMaterial")
class MeshLambertMaterial(parameters: MeshLambertMaterialParameters = js.native)
    extends Material {
  val color: Color    = js.native
  val emissive: Color = js.native
}

@js.native
trait MeshBasicMaterialParameters extends js.Object

@js.native
@JSImport("three", "MeshBasicMaterial")
class MeshBasicMaterial(parameters: MeshBasicMaterialParameters = js.native)
    extends Material {
  val color: Color = js.native
}

@js.native
@JSImport("three", "Texture")
class Texture(image: js.Any = js.native) extends js.Object {
  var wrapS: Int           = js.native
  var wrapT: Int           = js.native
  var needsUpdate: Boolean = js.native
}

@js.native
@JSImport("three", "Mesh")
class Mesh(geo: BufferGeometry, mat: Material) extends Object3D {
  var geometry: BufferGeometry = js.native
  var material: Material       = js.native
}

@js.native
trait Intersection extends js.Object {
  val `object`: Object3D = js.native
}

@js.native
@JSImport("three", "Raycaster")
class Raycaster extends js.Object {
  def setFromCamera(coords: Vector2, camera: Camera): Unit                  = js.native
  def intersectObjects(objects: js.Array[Object3D]): js.Array[Intersection] = js.native
}

@js.native
@JSImport("three", "BufferGeometryLoader")
class BufferGeometryLoader extends js.Object {
  def load(
        url: String,
        onLoad: js.Function1[BufferGeometry, Unit]
  ): Unit = js.native
}

@js.native
trait WebGLRendererParameters extends js.Object

@js.native
@JSImport("three", "WebGLRenderer")
class WebGLRenderer(parameters: WebGLRendererParameters = js.native) extends js.Object {
  val domElement: HTMLCanvasElement                                 = js.native
  def setSize(width: Double, height: Double): Unit                  = js.native
  def setViewport(x: Double, y: Double, w: Double, h: Double): Unit = js.native
  def setPixelRatio(value: Double): Unit                            = js.native
  def setClearColor(color: Color, alpha: Double): Unit              = js.native
  def render(scene: Scene, camera: Camera): Unit                    = js.native
}
