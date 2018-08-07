package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Interpolant")
abstract class Interpolant protected () extends js.Object {
  def this(parameterPositions: js.Any, samplesValues: js.Any, sampleSize: Double, resultBuffer: js.Any = js.native) = this()
  var parameterPositions: js.Any = js.native
  var samplesValues: js.Any = js.native
  var valueSize: Double = js.native
  var resultBuffer: js.Any = js.native
  def evaluate(time: Double): js.Dynamic = js.native
}