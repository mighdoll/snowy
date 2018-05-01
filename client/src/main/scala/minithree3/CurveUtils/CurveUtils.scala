package minithree3.CurveUtils

import scala.scalajs.js
@js.native
@js.annotation.JSGlobal("CurveUtils")
object CurveUtils extends js.Object {
  def tangentQuadraticBezier(t: Double, p0: Double, p1: Double, p2: Double): Double = js.native
  def tangentCubicBezier(t: Double, p0: Double, p1: Double, p2: Double, p3: Double): Double = js.native
  def tangentSpline(t: Double, p0: Double, p1: Double, p2: Double, p3: Double): Double = js.native
  def interpolate(p0: Double, p1: Double, p2: Double, p3: Double, t: Double): Double = js.native
}