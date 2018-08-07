package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
@js.annotation.JSGlobal("AnimationMixer")
class AnimationMixer protected () extends EventDispatcher {
  def this(root: js.Any) = this()
  var time: Double = js.native
  var timeScale: Double = js.native
  def clipAction(clip: AnimationClip, root: js.Any = js.native): AnimationAction = js.native
  def existingAction(clip: AnimationClip, root: js.Any = js.native): AnimationAction = js.native
  def stopAllAction(): AnimationMixer = js.native
  def update(deltaTime: Double): AnimationMixer = js.native
  def getRoot(): js.Dynamic = js.native
  def uncacheClip(clip: AnimationClip): Unit = js.native
  def uncacheRoot(root: js.Any): Unit = js.native
  def uncacheAction(clip: AnimationClip, root: js.Any = js.native): Unit = js.native
}