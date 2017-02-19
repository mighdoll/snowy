package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSBracketAccess, JSName}

@js.native
sealed trait Side extends js.Object {}

@js.native
@JSName("THREE.Side")
object Side extends js.Object {
  @JSBracketAccess
  def apply(value: Side): String = js.native
}
