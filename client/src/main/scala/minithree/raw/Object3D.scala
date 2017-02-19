package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Object3D")
class Object3D extends js.Object with EventDispatcher {
  var id: Double                                                              = js.native
  var uuid: String                                                            = js.native
  var name: String                                                            = js.native
  var parent: Object3D                                                        = js.native
  var children: js.Array[Object3D]                                            = js.native
  var up: Vector3                                                             = js.native
  var position: Vector3                                                       = js.native
  var rotation: Euler                                                         = js.native
  var quaternion: Quaternion                                                  = js.native
  var scale: Vector3                                                          = js.native
  var renderDepth: Double                                                     = js.native
  var rotationAutoUpdate: Boolean                                             = js.native
  var matrix: Matrix4                                                         = js.native
  var matrixWorld: Matrix4                                                    = js.native
  var matrixAutoUpdate: Boolean                                               = js.native
  var matrixWorldNeedsUpdate: Boolean                                         = js.native
  var visible: Boolean                                                        = js.native
  var castShadow: Boolean                                                     = js.native
  var receiveShadow: Boolean                                                  = js.native
  var frustumCulled: Boolean                                                  = js.native
  var userData: js.Any                                                        = js.native
  var eulerOrder: String                                                      = js.native
  def applyMatrix(matrix: Matrix4): Unit                                      = js.native
  def setRotationFromAxisAngle(axis: Vector3, angle: Double): Unit            = js.native
  def setRotationFromEuler(euler: Euler): Unit                                = js.native
  def setRotationFromMatrix(m: Matrix4): Unit                                 = js.native
  def setRotationFromQuaternion(q: Quaternion): Unit                          = js.native
  def rotateOnAxis(axis: Vector3, angle: Double): Object3D                    = js.native
  def rotateX(angle: Double): Object3D                                        = js.native
  def rotateY(angle: Double): Object3D                                        = js.native
  def rotateZ(angle: Double): Object3D                                        = js.native
  def translateOnAxis(axis: Vector3, distance: Double): Object3D              = js.native
  def translate(distance: Double, axis: Vector3): Object3D                    = js.native
  def translateX(distance: Double): Object3D                                  = js.native
  def translateY(distance: Double): Object3D                                  = js.native
  def translateZ(distance: Double): Object3D                                  = js.native
  def localToWorld(vector: Vector3): Vector3                                  = js.native
  def worldToLocal(vector: Vector3): Vector3                                  = js.native
  def lookAt(vector: Vector3): Unit                                           = js.native
  def add(`object`: Object3D): Unit                                           = js.native
  def remove(`object`: Object3D): Unit                                        = js.native
  def raycast(raycaster: Raycaster, intersects: js.Any): Unit                 = js.native
  def traverse(callback: js.Function1[Object3D, Any]): Unit                   = js.native
  def getObjectById(id: String, recursive: Boolean): Object3D                 = js.native
  def getObjectByName(name: String, recursive: Boolean = js.native): Object3D = js.native
  def getChildByName(name: String, recursive: Boolean = js.native): Object3D  = js.native
  def updateMatrix(): Unit                                                    = js.native
  def updateMatrixWorld(force: Boolean): Unit                                 = js.native
  def updateMatrixWorld(): Unit                                               = js.native
  def clone(`object`: Object3D = js.native, recursive: Boolean = js.native): Object3D =
    js.native
}
