package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("QuaternionKeyframeTrack")
class QuaternionKeyframeTrack protected () extends KeyframeTrack {
  def this(name: String, times: js.Array[js.Any], values: js.Array[js.Any], interpolation: InterpolationModes) = this()
}