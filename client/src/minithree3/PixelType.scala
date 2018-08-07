package minithree3

import scala.scalajs.js

@js.native
sealed trait PixelType extends js.Object {
}

@js.native
@js.annotation.JSGlobal("PixelType")
object PixelType extends js.Object {
  @js.annotation.JSBracketAccess
  def apply(value: PixelType): String = js.native
}