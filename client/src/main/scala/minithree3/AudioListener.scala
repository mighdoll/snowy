package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("AudioListener")
class AudioListener extends Object3D {
  var `type`: String = js.native
  var context: AudioContext = js.native
  var gain: GainNode = js.native
  def getInput(): GainNode = js.native
  def removeFilter(): Unit = js.native
  def setFilter(value: js.Any): Unit = js.native
  def getFilter(): js.Dynamic = js.native
  def setMasterVolume(value: Double): Unit = js.native
  def getMasterVolume(): Double = js.native
}