package minithree3

import scala.scalajs.js
import scala.scalajs.js.typedarray._

@js.native
@js.annotation.JSGlobal("Skeleton")
class Skeleton protected () extends js.Object {
  def this(bones: js.Array[Bone], boneInverses: js.Array[Matrix4] = js.native) = this()
  /**
     * @deprecated This property has been removed completely.
     */
  var useVertexTexture: Boolean = js.native
  var identityMatrix: Matrix4 = js.native
  var bones: js.Array[Bone] = js.native
  var boneTextureWidth: Double = js.native
  var boneTextureHeight: Double = js.native
  var boneMatrices: Float32Array = js.native
  var boneTexture: DataTexture = js.native
  var boneInverses: js.Array[Matrix4] = js.native
  def calculateInverses(bone: Bone): Unit = js.native
  def pose(): Unit = js.native
  def update(): Unit = js.native
}