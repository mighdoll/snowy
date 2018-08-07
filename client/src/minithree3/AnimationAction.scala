package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("AnimationAction")
class AnimationAction extends js.Object {
  var loop: Boolean = js.native
  var time: Double = js.native
  var timeScale: Double = js.native
  var weight: Double = js.native
  var repetitions: Double = js.native
  var paused: Boolean = js.native
  var enabled: Boolean = js.native
  var clampWhenFinished: Boolean = js.native
  var zeroSlopeAtStart: Boolean = js.native
  var zeroSlopeAtEnd: Boolean = js.native
  def play(): AnimationAction = js.native
  def stop(): AnimationAction = js.native
  def reset(): AnimationAction = js.native
  def isRunning(): Boolean = js.native
  def startAt(time: Double): AnimationAction = js.native
  def setLoop(mode: AnimationActionLoopStyles, repetitions: Double): AnimationAction = js.native
  def setEffectiveWeight(weight: Double): AnimationAction = js.native
  def getEffectiveWeight(): Double = js.native
  def fadeIn(duration: Double): AnimationAction = js.native
  def fadeOut(duration: Double): AnimationAction = js.native
  def crossFadeFrom(fadeOutAction: AnimationAction, duration: Double, warp: Boolean): AnimationAction = js.native
  def crossFadeTo(fadeInAction: AnimationAction, duration: Double, warp: Boolean): AnimationAction = js.native
  def stopFading(): AnimationAction = js.native
  def setEffectiveTimeScale(timeScale: Double): AnimationAction = js.native
  def getEffectiveTimeScale(): Double = js.native
  def setDuration(duration: Double): AnimationAction = js.native
  def syncWith(action: AnimationAction): AnimationAction = js.native
  def halt(duration: Double): AnimationAction = js.native
  def warp(statTimeScale: Double, endTimeScale: Double, duration: Double): AnimationAction = js.native
  def stopWarping(): AnimationAction = js.native
  def getMixer(): AnimationMixer = js.native
  def getClip(): AnimationClip = js.native
  def getRoot(): js.Dynamic = js.native
}