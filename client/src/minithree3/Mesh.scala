package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("Mesh")
class Mesh protected () extends Object3D {
  def this(geometry: Geometry | BufferGeometry = js.native, material: Material | js.Array[Material] = js.native) = this()
  var geometry: Geometry | BufferGeometry = js.native
  var material: Material | js.Array[Material] = js.native
  var drawMode: TrianglesDrawModes = js.native
  var morphTargetInfluences: js.Array[Double] = js.native
  var morphTargetDictionary: js.Dictionary[Double] = js.native
  var isMesh: Boolean = js.native
  def setDrawMode(drawMode: TrianglesDrawModes): Unit = js.native
  def updateMorphTargets(): Unit = js.native
  def getMorphTargetIndexByName(name: String): Double = js.native
  //def raycast(raycaster: Raycaster, intersects: js.Any): Unit = js.native
}