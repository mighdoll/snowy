package minithree3

import scala.scalajs.js

@js.native
trait IFog extends js.Object {
  var name: String = js.native
  var color: Color = js.native
  def toJSON(): js.Dynamic = js.native
}