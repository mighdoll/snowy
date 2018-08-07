package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("TetrahedronGeometry")
class TetrahedronGeometry protected () extends PolyhedronGeometry {
  def this(radius: Double = js.native, detail: Double = js.native) = this()
}