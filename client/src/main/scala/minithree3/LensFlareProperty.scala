package minithree3

import scala.scalajs.js

@js.native
trait LensFlareProperty extends js.Object {
  var texture: Texture = js.native
  // Texture
  var size: Double = js.native
  // size in pixels (-1 = use texture.width)
  var distance: Double = js.native
  // distance (0-1) from light source (0=at light source)
  var x: Double = js.native
  var y: Double = js.native
  var z: Double = js.native
  // screen position (-1 =>  1) z = 0 is ontop z = 1 is back
  var scale: Double = js.native
  // scale
  var rotation: Double = js.native
  // rotation
  var opacity: Double = js.native
  // opacity
  var color: Color = js.native
  // color
  var blending: Blending = js.native
}