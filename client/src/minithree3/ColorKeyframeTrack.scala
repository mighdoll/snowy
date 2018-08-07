package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("ColorKeyframeTrack")
class ColorKeyframeTrack protected () extends KeyframeTrack {
  def this(name: String, times: js.Array[js.Any], values: js.Array[js.Any], interpolation: InterpolationModes) = this()
}