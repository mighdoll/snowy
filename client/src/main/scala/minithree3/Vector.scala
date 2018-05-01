package minithree3

import scala.scalajs.js

@js.native
trait Vector extends js.Object {
  def setComponent(index: Double, value: Double): Unit = js.native
  def getComponent(index: Double): Double = js.native
  /**
     * copy(v:T):T;
     */
  def copy(v: Vector): js.Dynamic = js.native
  /**
     * add(v:T):T;
     */
  def add(v: Vector): Vector = js.native
  /**
     * addVectors(a:T, b:T):T;
     */
  def addVectors(a: Vector, b: Vector): Vector = js.native
  /**
     * sub(v:T):T;
     */
  def sub(v: Vector): Vector = js.native
  /**
     * subVectors(a:T, b:T):T;
     */
  def subVectors(a: Vector, b: Vector): Vector = js.native
  /**
     * multiplyScalar(s:number):T;
     */
  def multiplyScalar(s: Double): Vector = js.native
  /**
     * divideScalar(s:number):T;
     */
  def divideScalar(s: Double): Vector = js.native
  /**
     * negate():T;
     */
  def negate(): Vector = js.native
  /**
     * dot(v:T):T;
     */
  def dot(v: Vector): Double = js.native
  /**
     * lengthSq():number;
     */
  def lengthSq(): Double = js.native
  /**
     * length():number;
     */
  def length(): Double = js.native
  /**
     * normalize():T;
     */
  def normalize(): Vector = js.native
  /**
     * NOTE: Vector4 doesn't have the property.
     *
     * distanceTo(v:T):number;
     */
  def distanceTo(v: Vector): Double = js.native
  /**
     * NOTE: Vector4 doesn't have the property.
     *
     * distanceToSquared(v:T):number;
     */
  def distanceToSquared(v: Vector): Double = js.native
  /**
     * setLength(l:number):T;
     */
  def setLength(l: Double): Vector = js.native
  /**
     * lerp(v:T, alpha:number):T;
     */
  def lerp(v: Vector, alpha: Double): Vector = js.native
  /**
     * equals(v:T):boolean;
     */
  def equals(v: Vector): Boolean = js.native
}