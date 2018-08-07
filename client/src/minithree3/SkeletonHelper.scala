package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("SkeletonHelper")
class SkeletonHelper protected () extends LineSegments {
  def this(bone: Object3D) = this()
  var bones: js.Array[Bone] = js.native
  var root: Object3D = js.native
  def getBoneList(`object`: Object3D): js.Array[Bone] = js.native
  def update(): Unit = js.native
}