package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Camera")
class Camera extends Object3D {
  /**
     * This constructor sets following properties to the correct type: matrixWorldInverse, projectionMatrix and projectionMatrixInverse.
     */
  /**
     * This is the inverse of matrixWorld. MatrixWorld contains the Matrix which has the world transform of the Camera.
     */
  var matrixWorldInverse: Matrix4 = js.native
  /**
     * This is the matrix which contains the projection.
     */
  var projectionMatrix: Matrix4 = js.native
  //def getWorldDirection(target: Vector3): Vector3 = js.native
}