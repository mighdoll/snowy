package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSBracketAccess, JSName}

@js.native
sealed trait PixelFormat extends js.Object {}

@js.native
@JSName("THREE.PixelFormat")
object PixelFormat extends js.Object {
  @JSBracketAccess
  def apply(value: PixelFormat): String = js.native
}

@js.native
sealed trait PixelType extends js.Object {}

@js.native
@JSName("THREE.PixelType")
object PixelType extends js.Object {
  @JSBracketAccess
  def apply(value: PixelType): String = js.native
}
