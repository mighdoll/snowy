package minithree3

import scala.scalajs.js

@js.native
sealed trait Wrapping extends js.Object {
}

@js.native
@js.annotation.JSGlobal("Wrapping")
object Wrapping extends js.Object {
  @js.annotation.JSBracketAccess
  def apply(value: Wrapping): String = js.native
}