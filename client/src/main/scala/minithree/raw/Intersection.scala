package minithree.raw

import scala.scalajs.js

@js.native
trait Intersection extends js.Object {
  var distance: Double   = js.native
  var point: Vector3     = js.native
  var face: Face3        = js.native
  var `object`: Object3D = js.native
}
