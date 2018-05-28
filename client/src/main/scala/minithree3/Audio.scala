package minithree3

import org.scalajs.dom.{AudioBufferSourceNode, AudioContext, GainNode}

import scala.scalajs.js

//noinspection SyntaxError
@js.native
@js.annotation.JSGlobal("Audio")
class Audio protected () extends Object3D {
  def this(listener: AudioListener) = this()
  //var `type`: String = js.native
  var context: AudioContext = js.native
  var source: AudioBufferSourceNode = js.native
  var gain: GainNode = js.native
  var autoplay: Boolean = js.native
  var startTime: Double = js.native
  var playbackRate: Double = js.native
  var hasPlaybackControl: Boolean = js.native
  var isPlaying: Boolean = js.native
  var sourceType: String = js.native
  var filters: js.Array[js.Any] = js.native
  def getOutput(): GainNode = js.native
  def setNodeSource(audioNode: AudioBufferSourceNode): Audio = js.native
  def setBuffer(audioBuffer: AudioBuffer): Audio = js.native
  def play(): Audio = js.native
  def pause(): Audio = js.native
  def stop(): Audio = js.native
  def connect(): Audio = js.native
  def disconnect(): Audio = js.native
  def getFilters(): js.Array[js.Any] = js.native
  def setFilter(value: js.Array[js.Any]): Audio = js.native
  def getFilter(): js.Dynamic = js.native
  def setFilter(filter: js.Any): Audio = js.native
  def setPlaybackRate(value: Double): Audio = js.native
  def getPlaybackRate(): Double = js.native
  def onEnded(): Unit = js.native
  def getLoop(): Boolean = js.native
  def setLoop(value: Boolean): Unit = js.native
  def getVolume(): Double = js.native
  def setVolume(value: Double): Audio = js.native
  /**
     * @deprecated Use { AudioLoader} instead.
     */
  def load(file: String): Audio = js.native
}