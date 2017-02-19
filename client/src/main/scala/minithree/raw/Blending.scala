package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSBracketAccess, JSName}

@js.native
sealed trait Blending extends js.Object {}

@js.native
@JSName("THREE.Blending")
object Blending extends js.Object {
  @JSBracketAccess
  def apply(value: Blending): String = js.native
}

@js.native
sealed trait BlendingEquation extends js.Object {}

@js.native
@JSName("THREE.BlendingEquation")
object BlendingEquation extends js.Object {
  @JSBracketAccess
  def apply(value: BlendingEquation): String = js.native
}

@js.native
sealed trait BlendingDstFactor extends js.Object {}

@js.native
@JSName("THREE.BlendingDstFactor")
object BlendingDstFactor extends js.Object {
  @JSBracketAccess
  def apply(value: BlendingDstFactor): String = js.native
}

@js.native
sealed trait BlendingSrcFactor extends js.Object {}

@js.native
@JSName("THREE.BlendingSrcFactor")
object BlendingSrcFactor extends js.Object {
  @JSBracketAccess
  def apply(value: BlendingSrcFactor): String = js.native
}
