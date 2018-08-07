package minithree3

import scala.scalajs.js

@js.native
trait MeshPhysicalMaterialParameters extends MeshStandardMaterialParameters {
  var reflectivity: Double = js.native
  var clearCoat: Double = js.native
  var clearCoatRoughness: Double = js.native
}