package minithree3

import scala.scalajs.js

@js.native
sealed trait MOUSE extends js.Object {
}

@js.native
@js.annotation.JSGlobal("MOUSE")
object MOUSE extends js.Object {
  val LEFT: MOUSE = js.native
  val MIDDLE: MOUSE = js.native
  val RIGHT: MOUSE = js.native
  @js.annotation.JSBracketAccess
  def apply(value: MOUSE): String = js.native
}