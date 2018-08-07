package minithree3

import scala.scalajs.js

@js.native
trait Intersection extends js.Object {
  var distance: Double = js.native
  var distanceToRay: Double = js.native
  var point: Vector3 = js.native
  var index: Double = js.native
  var face: Face3 = js.native
  var faceIndex: Double = js.native
  var `object`: Object3D = js.native
}