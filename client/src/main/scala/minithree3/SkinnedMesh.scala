package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("SkinnedMesh")
class SkinnedMesh protected () extends Mesh {
  def this(geometry: Geometry | BufferGeometry = js.native, material: MeshBasicMaterial = js.native, useVertexTexture: Boolean = js.native) = this()
  def this(geometry: Geometry | BufferGeometry = js.native, material: MeshDepthMaterial = js.native, useVertexTexture: Boolean = js.native) = this()
  def this(geometry: Geometry | BufferGeometry = js.native, material: MultiMaterial = js.native, useVertexTexture: Boolean = js.native) = this()
  def this(geometry: Geometry | BufferGeometry = js.native, material: MeshLambertMaterial = js.native, useVertexTexture: Boolean = js.native) = this()
  def this(geometry: Geometry | BufferGeometry = js.native, material: MeshNormalMaterial = js.native, useVertexTexture: Boolean = js.native) = this()
  def this(geometry: Geometry | BufferGeometry = js.native, material: MeshPhongMaterial = js.native, useVertexTexture: Boolean = js.native) = this()
  def this(geometry: Geometry | BufferGeometry = js.native, material: ShaderMaterial = js.native, useVertexTexture: Boolean = js.native) = this()
  var bindMode: String = js.native
  var bindMatrix: Matrix4 = js.native
  var bindMatrixInverse: Matrix4 = js.native
  var skeleton: Skeleton = js.native
  def bind(skeleton: Skeleton, bindMatrix: Matrix4 = js.native): Unit = js.native
  def pose(): Unit = js.native
  def normalizeSkinWeights(): Unit = js.native
  def updateMatrixWorld(force: Boolean = js.native): Unit = js.native
}