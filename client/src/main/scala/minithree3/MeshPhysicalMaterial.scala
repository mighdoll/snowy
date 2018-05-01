package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("MeshPhysicalMaterial")
class MeshPhysicalMaterial protected () extends MeshStandardMaterial {
  def this(parameters: MeshPhysicalMaterialParameters) = this()
  var defines: js.Any = js.native
  var reflectivity: Double = js.native
  var clearCoat: Double = js.native
  var clearCoatRoughness: Double = js.native
}