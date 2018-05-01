package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("CurvePath")
class CurvePath[T <: Vector] extends Curve[T] {
  var curves: js.Array[Curve[T]] = js.native
  var autoClose: Boolean = js.native
  def add(curve: Curve[T]): Unit = js.native
  def checkConnection(): Boolean = js.native
  def closePath(): Unit = js.native
  def getPoint(t: Double): T = js.native
  def getLength(): Double = js.native
  def updateArcLengths(): Unit = js.native
  def getCurveLengths(): js.Array[Double] = js.native
  def getSpacedPoints(divisions: Double = js.native): js.Array[T] = js.native
  def getPoints(divisions: Double = js.native): js.Array[T] = js.native
  /**
     * @deprecated Use { Geometry#setFromPoints new THREE.Geometry().setFromPoints( points )} instead.
     */
  def createPointsGeometry(divisions: Double): Geometry = js.native
  /**
     * @deprecated Use { Geometry#setFromPoints new THREE.Geometry().setFromPoints( points )} instead.
     */
  def createSpacedPointsGeometry(divisions: Double): Geometry = js.native
  /**
     * @deprecated Use { Geometry#setFromPoints new THREE.Geometry().setFromPoints( points )} instead.
     */
  def createGeometry(points: js.Array[T]): Geometry = js.native
}