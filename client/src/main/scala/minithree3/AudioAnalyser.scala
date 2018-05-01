package minithree3

import scala.scalajs.js
import scala.scalajs.js.typedarray._

@js.native
@js.annotation.JSGlobal("AudioAnalyser")
class AudioAnalyser protected () extends js.Object {
  def this(audio: js.Any, fftSize: Double) = this()
  var analyser: js.Any = js.native
  var data: Uint8Array = js.native
  def getFrequencyData(): Uint8Array = js.native
  def getAverageFrequency(): Double = js.native
  /**
     * @deprecated Use { AudioAnalyser#getFrequencyData .getFrequencyData()} instead.
     */
  def getData(file: js.Any): js.Dynamic = js.native
}