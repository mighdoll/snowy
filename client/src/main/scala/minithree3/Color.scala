package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Color")
class Color protected () extends js.Object {
  def this(color: Color) = this()
  def this(color: String) = this()
  def this(color: Double) = this()
  def this(r: Double, g: Double, b: Double) = this()
  /**
     * Red channel value between 0 and 1. Default is 1.
     */
  var r: Double = js.native
  /**
     * Green channel value between 0 and 1. Default is 1.
     */
  var g: Double = js.native
  /**
     * Blue channel value between 0 and 1. Default is 1.
     */
  var b: Double = js.native
  def set(color: Color): Color = js.native
  def set(color: Double): Color = js.native
  def set(color: String): Color = js.native
  def setScalar(scalar: Double): Color = js.native
  def setHex(hex: Double): Color = js.native
  /**
     * Sets this color from RGB values.
     * @param r Red channel value between 0 and 1.
     * @param g Green channel value between 0 and 1.
     * @param b Blue channel value between 0 and 1.
     */
  def setRGB(r: Double, g: Double, b: Double): Color = js.native
  /**
     * Sets this color from HSL values.
     * Based on MochiKit implementation by Bob Ippolito.
     *
     * @param h Hue channel value between 0 and 1.
     * @param s Saturation value channel between 0 and 1.
     * @param l Value channel value between 0 and 1.
     */
  def setHSL(h: Double, s: Double, l: Double): Color = js.native
  /**
     * Sets this color from a CSS context style string.
     * @param contextStyle Color in CSS context style format.
     */
  def setStyle(style: String): Color = js.native
  /**
     * Clones this color.
     */
  /**
     * Copies given color.
     * @param color Color to copy.
     */
  def copy(color: Color): js.Dynamic = js.native
  /**
     * Copies given color making conversion from gamma to linear space.
     * @param color Color to copy.
     */
  def copyGammaToLinear(color: Color, gammaFactor: Double = js.native): Color = js.native
  /**
     * Copies given color making conversion from linear to gamma space.
     * @param color Color to copy.
     */
  def copyLinearToGamma(color: Color, gammaFactor: Double = js.native): Color = js.native
  /**
     * Converts this color from gamma to linear space.
     */
  def convertGammaToLinear(): Color = js.native
  /**
     * Converts this color from linear to gamma space.
     */
  def convertLinearToGamma(): Color = js.native
  /**
     * Returns the hexadecimal value of this color.
     */
  def getHex(): Double = js.native
  /**
     * Returns the string formated hexadecimal value of this color.
     */
  def getHexString(): String = js.native
  def getHSL(): HSL = js.native
  /**
     * Returns the value of this color in CSS context style.
     * Example: rgb(r, g, b)
     */
  def getStyle(): String = js.native
  def offsetHSL(h: Double, s: Double, l: Double): Color = js.native
  def add(color: Color): Color = js.native
  def addColors(color1: Color, color2: Color): Color = js.native
  def addScalar(s: Double): Color = js.native
  def sub(color: Color): Color = js.native
  def multiply(color: Color): Color = js.native
  def multiplyScalar(s: Double): Color = js.native
  def lerp(color: Color, alpha: Double): Color = js.native
  def equals(color: Color): Boolean = js.native
  def fromArray(rgb: js.Array[Double], offset: Double = js.native): Color = js.native
  def toArray(array: js.Array[Double] = js.native, offset: Double = js.native): js.Array[Double] = js.native
}