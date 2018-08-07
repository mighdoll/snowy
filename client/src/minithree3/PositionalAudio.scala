package minithree3

import org.scalajs.dom.PannerNode

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("PositionalAudio")
class PositionalAudio protected () extends Audio {
  def this(listener: AudioListener) = this()
  var panner: PannerNode = js.native
  def setRefDistance(value: Double): Unit = js.native
  def getRefDistance(): Double = js.native
  def setRolloffFactor(value: Double): Unit = js.native
  def getRolloffFactor(): Double = js.native
  def setDistanceModel(value: Double): Unit = js.native
  def getDistanceModel(): Double = js.native
  def setMaxDistance(value: Double): Unit = js.native
  def getMaxDistance(): Double = js.native
}