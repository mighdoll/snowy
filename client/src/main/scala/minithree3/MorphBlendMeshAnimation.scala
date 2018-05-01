package minithree3

import scala.scalajs.js

@js.native
trait MorphBlendMeshAnimation extends js.Object {
  var start: Double = js.native
  var end: Double = js.native
  var length: Double = js.native
  var fps: Double = js.native
  var duration: Double = js.native
  var lastFrame: Double = js.native
  var currentFrame: Double = js.native
  var active: Boolean = js.native
  var time: Double = js.native
  var direction: Double = js.native
  var weight: Double = js.native
  var directionBackwards: Boolean = js.native
  var mirroredLoop: Boolean = js.native
}