package minithree3

import scala.scalajs.js
import scala.scalajs.js.typedarray._

@js.native
@js.annotation.JSGlobal("KeyframeTrack")
class KeyframeTrack protected () extends js.Object {
  def this(name: String, times: js.Array[js.Any], values: js.Array[js.Any], interpolation: InterpolationModes) = this()
  var name: String = js.native
  var times: js.Array[js.Any] = js.native
  var values: js.Array[js.Any] = js.native
  var ValueTypeName: String = js.native
  var TimeBufferType: Float32Array = js.native
  var ValueBufferType: Float32Array = js.native
  var DefaultInterpolation: InterpolationModes = js.native
  def InterpolantFactoryMethodDiscrete(result: js.Any): DiscreteInterpolant = js.native
  def InterpolantFactoryMethodLinear(result: js.Any): LinearInterpolant = js.native
  def InterpolantFactoryMethodSmooth(result: js.Any): CubicInterpolant = js.native
  def setInterpolation(interpolation: InterpolationModes): Unit = js.native
  def getInterpolation(): InterpolationModes = js.native
  def getValuesize(): Double = js.native
  def shift(timeOffset: Double): KeyframeTrack = js.native
  def scale(timeScale: Double): KeyframeTrack = js.native
  def trim(startTime: Double, endTime: Double): KeyframeTrack = js.native
  def validate(): Boolean = js.native
  def optimize(): KeyframeTrack = js.native
}

@js.native
@js.annotation.JSGlobal("KeyframeTrack")
object KeyframeTrack extends js.Object {
  def parse(json: js.Any): KeyframeTrack = js.native
  def toJSON(track: KeyframeTrack): js.Dynamic = js.native
}