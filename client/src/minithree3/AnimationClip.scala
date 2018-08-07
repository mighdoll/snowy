package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("AnimationClip")
class AnimationClip protected () extends js.Object {
  def this(name: String = js.native, duration: Double = js.native, tracks: js.Array[KeyframeTrack] = js.native) = this()
  var name: String = js.native
  var tracks: js.Array[KeyframeTrack] = js.native
  var duration: Double = js.native
  var uuid: String = js.native
  var results: js.Array[js.Any] = js.native
  def resetDuration(): Unit = js.native
  def trim(): AnimationClip = js.native
  def optimize(): AnimationClip = js.native
}

@js.native
@js.annotation.JSGlobal("AnimationClip")
object AnimationClip extends js.Object {
  def CreateFromMorphTargetSequence(name: String, morphTargetSequence: js.Array[MorphTarget], fps: Double, noLoop: Boolean): AnimationClip = js.native
  def findByName(clipArray: js.Array[AnimationClip], name: String): AnimationClip = js.native
  def CreateClipsFromMorphTargetSequences(morphTargets: js.Array[MorphTarget], fps: Double, noLoop: Boolean): js.Array[AnimationClip] = js.native
  def parse(json: js.Any): AnimationClip = js.native
  def parseAnimation(animation: js.Any, bones: js.Array[Bone], nodeName: String): AnimationClip = js.native
  def toJSON(): js.Dynamic = js.native
}