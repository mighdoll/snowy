package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("Object3D")
class Object3D extends EventDispatcher {
  /**
     * Unique number of this object instance.
     */
  var id: Double = js.native
  /**
     *
     */
  var uuid: String = js.native
  /**
     * Optional name of the object (doesn't need to be unique).
     */
  var name: String = js.native
  var `type`: String = js.native
  /**
     * Object's parent in the scene graph.
     */
  var parent: Object3D = js.native
  /**
     * Array with object's children.
     */
  var children: js.Array[Object3D] = js.native
  /**
     * Up direction.
     */
  var up: Vector3 = js.native
  /**
     * Object's local position.
     */
  var position: Vector3 = js.native
  /**
     * Object's local rotation (Euler angles), in radians.
     */
  var rotation: Euler = js.native
  /**
     * Global rotation.
     */
  var quaternion: Quaternion = js.native
  /**
     * Object's local scale.
     */
  var scale: Vector3 = js.native
  var modelViewMatrix: Matrix4 = js.native
  var normalMatrix: Matrix3 = js.native
  /**
     * Local transform.
     */
  var matrix: Matrix4 = js.native
  /**
     * The global transform of the object. If the Object3d has no parent, then it's identical to the local transform.
     */
  var matrixWorld: Matrix4 = js.native
  /**
     * When this is set, it calculates the matrix of position, (rotation or quaternion) and scale every frame and also recalculates the matrixWorld property.
     */
  var matrixAutoUpdate: Boolean = js.native
  /**
     * When this is set, it calculates the matrixWorld in that frame and resets this property to false.
     */
  var matrixWorldNeedsUpdate: Boolean = js.native
  var layers: Layers = js.native
  /**
     * Object gets rendered if true.
     */
  var visible: Boolean = js.native
  /**
     * Gets rendered into shadow map.
     */
  var castShadow: Boolean = js.native
  /**
     * Material gets baked in shadow receiving.
     */
  var receiveShadow: Boolean = js.native
  /**
     * When this is set, it checks every frame if the object is in the frustum of the camera. Otherwise the object gets drawn every frame even if it isn't visible.
     */
  var frustumCulled: Boolean = js.native
  var renderOrder: Double = js.native
  /**
     * An object that can be used to store custom data about the Object3d. It should not hold references to functions as these will not be cloned.
     */
  var userData: js.Any = js.native
  /**
     * Used to check whether this or derived classes are Object3Ds. Default is true.
     * You should not change this, as it is used internally for optimisation.
     */
  var isObject3D: Boolean = js.native
  /**
     * Calls before rendering object
     */
  var onBeforeRender: js.Function6[WebGLRenderer, Scene, Camera, Geometry | BufferGeometry, Material, Group, Unit] = js.native
  /**
     * Calls after rendering object
     */
  var onAfterRender: js.Function6[WebGLRenderer, Scene, Camera, Geometry | BufferGeometry, Material, Group, Unit] = js.native
  /**
     *
     */
  /**
     * This updates the position, rotation and scale with the matrix.
     */
  def applyMatrix(matrix: Matrix4): Unit = js.native
  /**
     *
     */
  def setRotationFromAxisAngle(axis: Vector3, angle: Double): Unit = js.native
  /**
     *
     */
  def setRotationFromEuler(euler: Euler): Unit = js.native
  /**
     *
     */
  def setRotationFromMatrix(m: Matrix4): Unit = js.native
  /**
     *
     */
  def setRotationFromQuaternion(q: Quaternion): Unit = js.native
  /**
     * Rotate an object along an axis in object space. The axis is assumed to be normalized.
     * @param axis  A normalized vector in object space.
     * @param angle  The angle in radians.
     */
  def rotateOnAxis(axis: Vector3, angle: Double): Object3D = js.native
  /**
     * Rotate an object along an axis in world space. The axis is assumed to be normalized. Method Assumes no rotated parent.
     * @param axis  A normalized vector in object space.
     * @param angle  The angle in radians.
     */
  def rotateOnWorldAxis(axis: Vector3, angle: Double): Object3D = js.native
  /**
     *
     * @param angle
     */
  def rotateX(angle: Double): Object3D = js.native
  /**
     *
     * @param angle
     */
  def rotateY(angle: Double): Object3D = js.native
  /**
     *
     * @param angle
     */
  def rotateZ(angle: Double): Object3D = js.native
  /**
     * @param axis  A normalized vector in object space.
     * @param distance  The distance to translate.
     */
  def translateOnAxis(axis: Vector3, distance: Double): Object3D = js.native
  /**
     * Translates object along x axis by distance.
     * @param distance Distance.
     */
  def translateX(distance: Double): Object3D = js.native
  /**
     * Translates object along y axis by distance.
     * @param distance Distance.
     */
  def translateY(distance: Double): Object3D = js.native
  /**
     * Translates object along z axis by distance.
     * @param distance Distance.
     */
  def translateZ(distance: Double): Object3D = js.native
  /**
     * Updates the vector from local space to world space.
     * @param vector A local vector.
     */
  def localToWorld(vector: Vector3): Vector3 = js.native
  /**
     * Updates the vector from world space to local space.
     * @param vector A world vector.
     */
  def worldToLocal(vector: Vector3): Vector3 = js.native
  /**
     * Rotates object to face point in space.
     * @param vector A world vector to look at.
     */
  def lookAt(vector: Vector3): Unit = js.native
  def lookAt(x: Double, y: Double, z: Double): Unit = js.native
  /**
     * Adds object as child of this object.
     */
  def add(`object`: Object3D*): Unit = js.native
  /**
     * Removes object as child of this object.
     */
  def remove(`object`: Object3D*): Unit = js.native
  /**
     * Searches through the object's children and returns the first with a matching id, optionally recursive.
     * @param id  Unique number of the object instance
     */
  def getObjectById(id: Double): Object3D = js.native
  /**
     * Searches through the object's children and returns the first with a matching name, optionally recursive.
     * @param name  String to match to the children's Object3d.name property.
     */
  def getObjectByName(name: String): Object3D = js.native
  def getObjectByProperty(name: String, value: String): Object3D = js.native
  def getWorldPosition(target: Vector3): Vector3 = js.native
  def getWorldQuaternion(target: Quaternion): Quaternion = js.native
  def getWorldScale(target: Vector3): Vector3 = js.native
  def getWorldDirection(target: Vector3): Vector3 = js.native
  def raycast(raycaster: Raycaster, intersects: js.Any): Unit = js.native
  def traverse(callback: js.Function1[Object3D, Any]): Unit = js.native
  def traverseVisible(callback: js.Function1[Object3D, Any]): Unit = js.native
  def traverseAncestors(callback: js.Function1[Object3D, Any]): Unit = js.native
  /**
     * Updates local transform.
     */
  def updateMatrix(): Unit = js.native
  /**
     * Updates global transform of the object and its children.
     */
  def updateMatrixWorld(force: Boolean): Unit = js.native
  def toJSON(meta: js.Any = js.native): js.Dynamic = js.native
  /**
     *
     * @param object
     * @param recursive
     */
  def copy(source: Object3D, recursive: Boolean = js.native): Object3D = js.native
}

@js.native
@js.annotation.JSGlobal("Object3D")
object Object3D extends js.Object {
  var DefaultUp: Vector3 = js.native
  var DefaultMatrixAutoUpdate: Boolean = js.native
}