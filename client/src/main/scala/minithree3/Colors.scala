package minithree3

import scala.scalajs.js

@js.native
sealed trait Colors extends js.Object {
}

@js.native
@js.annotation.JSGlobal("Colors")
object Colors extends js.Object {
  @js.annotation.JSBracketAccess
  def apply(value: Colors): String = js.native
}