package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("MeshStandardMaterial")
class MeshStandardMaterial protected () extends Material {
  def this(parameters: MeshStandardMaterialParameters = js.native) = this()
  var defines: js.Any = js.native
  var color: Color = js.native
  var roughness: Double = js.native
  var metalness: Double = js.native
  var map: Texture = js.native
  var lightMap: Texture = js.native
  var lightMapIntensity: Double = js.native
  var aoMap: Texture = js.native
  var aoMapIntensity: Double = js.native
  var emissive: Color = js.native
  var emissiveIntensity: Double = js.native
  var emissiveMap: Texture = js.native
  var bumpMap: Texture = js.native
  var bumpScale: Double = js.native
  var normalMap: Texture = js.native
  var normalScale: Double = js.native
  var displacementMap: Texture = js.native
  var displacementScale: Double = js.native
  var displacementBias: Double = js.native
  var roughnessMap: Texture = js.native
  var metalnessMap: Texture = js.native
  var alphaMap: Texture = js.native
  var envMap: Texture = js.native
  var envMapIntensity: Double = js.native
  var refractionRatio: Double = js.native
  var wireframe: Boolean = js.native
  var wireframeLinewidth: Double = js.native
  var skinning: Boolean = js.native
  var morphTargets: Boolean = js.native
  var morphNormals: Boolean = js.native
  def setValues(parameters: MeshStandardMaterialParameters): Unit = js.native
}