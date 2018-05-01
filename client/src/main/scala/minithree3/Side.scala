package minithree3

import scala.scalajs.js

@js.native
sealed trait Side extends js.Object {
}

@js.native
@js.annotation.JSGlobal("Side")
object Side extends js.Object {
  @js.annotation.JSBracketAccess
  def apply(value: Side): String = js.native
}