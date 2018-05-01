package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Shape")
class Shape protected () extends Path {
  def this(points: js.Array[Vector2] = js.native) = this()
  var holes: js.Array[Path] = js.native
  /**
     * @deprecated Use { ExtrudeGeometry ExtrudeGeometry()} instead.
     */
  def extrude(options: js.Any = js.native): ExtrudeGeometry = js.native
  /**
     * @deprecated Use { ShapeGeometry ShapeGeometry()} instead.
     */
  def makeGeometry(options: js.Any = js.native): ShapeGeometry = js.native
  def getPointsHoles(divisions: Double): js.Array[js.Array[Vector2]] = js.native
  /**
     * @deprecated Use { Shape#extractPoints .extractPoints()} instead.
     */
  def extractAllPoints(divisions: Double): js.Any = js.native
  def extractPoints(divisions: Double): js.Array[Vector2] = js.native
}