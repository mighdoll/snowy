package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSBracketAccess, JSName}

@js.native
sealed trait Mapping extends js.Object {}

@js.native
@JSName("THREE.Mapping")
object Mapping extends js.Object {
  @JSBracketAccess
  def apply(value: Mapping): String = js.native
}

@js.native
trait MappingConstructor extends js.Object {
  /* js.native ConstructorMember(FunSignature(List(),List(),Some(TypeRef(TypeName(Mapping),List())))) */
}
