package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Curve")
class Curve[T <: Vector] extends js.Object {
  /**
     * This value determines the amount of divisions when calculating the cumulative segment lengths of a curve via .getLengths. 
     * To ensure precision when using methods like .getSpacedPoints, it is recommended to increase .arcLengthDivisions if the curve is very large. 
     * Default is 200.
     */
  var arcLengthDivisions: Double = js.native
  /**
     * Returns a vector for point t of the curve where t is between 0 and 1
     * getPoint(t: number): T;
     */
  def getPoint(t: Double, optionalTarget: T = js.native): T = js.native
  /**
     * Returns a vector for point at relative position in curve according to arc length
     * getPointAt(u: number): T;
     */
  def getPointAt(u: Double, optionalTarget: T = js.native): T = js.native
  /**
     * Get sequence of points using getPoint( t )
     * getPoints(divisions?: number): T[];
     */
  def getPoints(divisions: Double = js.native): js.Array[T] = js.native
  /**
     * Get sequence of equi-spaced points using getPointAt( u )
     * getSpacedPoints(divisions?: number): T[];
     */
  def getSpacedPoints(divisions: Double = js.native): js.Array[T] = js.native
  /**
     * Get total curve arc length
     */
  def getLength(): Double = js.native
  /**
     * Get list of cumulative segment lengths
     */
  def getLengths(divisions: Double = js.native): js.Array[Double] = js.native
  /**
     * Update the cumlative segment distance cache
     */
  def updateArcLengths(): Unit = js.native
  /**
     * Given u ( 0 .. 1 ), get a t to find p. This gives you points which are equi distance
     */
  def getUtoTmapping(u: Double, distance: Double): Double = js.native
  /**
     * Returns a unit vector tangent at t. If the subclassed curve do not implement its tangent derivation, 2 points a small delta apart will be used to find its gradient which seems to give a reasonable approximation
     * getTangent(t: number): T;
     */
  def getTangent(t: Double): T = js.native
  /**
     * Returns tangent at equidistance point u on the curve
     * getTangentAt(u: number): T;
     */
  def getTangentAt(u: Double): T = js.native
}
  /**
     * @deprecated since r84.
     */

@js.native
@js.annotation.JSGlobal("Curve")
object Curve extends js.Object {
  def create(constructorFunc: js.Function, getPointFunc: js.Function): js.Function = js.native
}