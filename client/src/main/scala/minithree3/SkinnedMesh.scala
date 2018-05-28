package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("SkinnedMesh")
class SkinnedMesh protected () extends Mesh {
  def this(geometry: Geometry | BufferGeometry, material: MeshBasicMaterial, useVertexTexture: Boolean) = this()
  def this(geometry: Geometry | BufferGeometry, material: MeshDepthMaterial, useVertexTexture: Boolean) = this()
  def this(geometry: Geometry | BufferGeometry, material: MultiMaterial, useVertexTexture: Boolean) = this()
  def this(geometry: Geometry | BufferGeometry, material: MeshLambertMaterial, useVertexTexture: Boolean) = this()
  def this(geometry: Geometry | BufferGeometry, material: MeshNormalMaterial, useVertexTexture: Boolean) = this()
  def this(geometry: Geometry | BufferGeometry, material: MeshPhongMaterial, useVertexTexture: Boolean) = this()
  def this(geometry: Geometry | BufferGeometry, material: ShaderMaterial, useVertexTexture: Boolean) = this()
  var bindMode: String = js.native
  var bindMatrix: Matrix4 = js.native
  var bindMatrixInverse: Matrix4 = js.native
  var skeleton: Skeleton = js.native
  def bind(skeleton: Skeleton, bindMatrix: Matrix4 = js.native): Unit = js.native
  def pose(): Unit = js.native
  def normalizeSkinWeights(): Unit = js.native
  //def updateMatrixWorld(force: Boolean = js.native): Unit = js.native
}