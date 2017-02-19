package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait IFog extends js.Object {
  var name: String           = js.native
  var color: Color           = js.native
  override def clone(): IFog = js.native
}

@js.native
@JSName("THREE.Fog")
class Fog(color: Double, var near: Double = js.native, var far: Double = js.native)
    extends IFog {
  override def clone(): Fog = js.native
}

@js.native
@JSName("THREE.FogExp2")
class FogExp2(color: Double, var density: Double = js.native) extends IFog {
  override def clone(): FogExp2 = js.native
}
