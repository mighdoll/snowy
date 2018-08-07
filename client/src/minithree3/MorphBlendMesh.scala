package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("MorphBlendMesh")
class MorphBlendMesh protected () extends Mesh {
  def this(geometry: Geometry, material: Material) = this()
  var animationsMap: js.Dictionary[MorphBlendMeshAnimation] = js.native
  var animationsList: js.Array[MorphBlendMeshAnimation] = js.native
  def createAnimation(name: String, start: Double, end: Double, fps: Double): Unit = js.native
  def autoCreateAnimations(fps: Double): Unit = js.native
  def setAnimationDirectionForward(name: String): Unit = js.native
  def setAnimationDirectionBackward(name: String): Unit = js.native
  def setAnimationFPS(name: String, fps: Double): Unit = js.native
  def setAnimationDuration(name: String, duration: Double): Unit = js.native
  def setAnimationWeight(name: String, weight: Double): Unit = js.native
  def setAnimationTime(name: String, time: Double): Unit = js.native
  def getAnimationTime(name: String): Double = js.native
  def getAnimationDuration(name: String): Double = js.native
  def playAnimation(name: String): Unit = js.native
  def stopAnimation(name: String): Unit = js.native
  def update(delta: Double): Unit = js.native
}