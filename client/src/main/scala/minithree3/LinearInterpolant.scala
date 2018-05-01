package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LinearInterpolant")
class LinearInterpolant protected () extends Interpolant {
  def this(parameterPositions: js.Any, samplesValues: js.Any, sampleSize: Double, resultBuffer: js.Any = js.native) = this()
  def interpolate_(i1: Double, t0: Double, t: Double, t1: Double): js.Dynamic = js.native
}