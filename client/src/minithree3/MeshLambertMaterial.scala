package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("MeshLambertMaterial")
class MeshLambertMaterial protected () extends Material {
  def this(parameters: MeshLambertMaterialParameters = js.native) = this()
  var color: Color = js.native
  var emissive: Color = js.native
  var emissiveIntensity: Double = js.native
  var emissiveMap: Texture = js.native
  var map: Texture = js.native
  var lightMap: Texture = js.native
  var lightMapIntensity: Double = js.native
  var aoMap: Texture = js.native
  var aoMapIntensity: Double = js.native
  var specularMap: Texture = js.native
  var alphaMap: Texture = js.native
  var envMap: Texture = js.native
  var combine: Combine = js.native
  var reflectivity: Double = js.native
  var refractionRatio: Double = js.native
  var wireframe: Boolean = js.native
  var wireframeLinewidth: Double = js.native
  var wireframeLinecap: String = js.native
  var wireframeLinejoin: String = js.native
  var skinning: Boolean = js.native
  var morphTargets: Boolean = js.native
  var morphNormals: Boolean = js.native
  def setValues(parameters: MeshLambertMaterialParameters): Unit = js.native
}