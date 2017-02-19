package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait HSL extends js.Object {
  var h: Double = js.native
  var s: Double = js.native
  var l: Double = js.native
}

@js.native
@JSName("THREE.Color")
class Color(color: Color = js.native) extends js.Object {
  var r: Double = js.native
  var g: Double = js.native
  var b: Double = js.native

  def this(r: Double, g: Double, b: Double) = this()

  def this(hex: Double) = this()

  def set(color: Color): Color                          = js.native
  def setHex(hex: Double): Color                        = js.native
  def setRGB(r: Double, g: Double, b: Double): Color    = js.native
  def setHSL(h: Double, s: Double, l: Double): Color    = js.native
  def setStyle(style: String): Color                    = js.native
  def copy(color: Color): Color                         = js.native
  def copyGammaToLinear(color: Color): Color            = js.native
  def copyLinearToGamma(color: Color): Color            = js.native
  def convertGammaToLinear(): Color                     = js.native
  def convertLinearToGamma(): Color                     = js.native
  def getHex(): Double                                  = js.native
  def getHexString(): String                            = js.native
  def getHSL(): HSL                                     = js.native
  def getStyle(): String                                = js.native
  def offsetHSL(h: Double, s: Double, l: Double): Color = js.native
  def add(color: Color): Color                          = js.native
  def addColors(color1: Color, color2: Color): Color    = js.native
  def addScalar(s: Double): Color                       = js.native
  def multiply(color: Color): Color                     = js.native
  def multiplyScalar(s: Double): Color                  = js.native
  def lerp(color: Color, alpha: Double): Color          = js.native
  def equals(color: Color): Boolean                     = js.native
  def fromArray(rgb: js.Array[Double]): Color           = js.native
  def toArray(): js.Array[Double]                       = js.native
  override def clone(): Color                           = js.native
}

@js.native
@JSName("THREE.ColorKeywords")
class ColorKeywords extends js.Object {}

@js.native
@JSName("THREE.ColorKeywords")
object ColorKeywords extends js.Object {
  var aliceblue: Double            = js.native
  var antiquewhite: Double         = js.native
  var aqua: Double                 = js.native
  var aquamarine: Double           = js.native
  var azure: Double                = js.native
  var beige: Double                = js.native
  var bisque: Double               = js.native
  var black: Double                = js.native
  var blanchedalmond: Double       = js.native
  var blue: Double                 = js.native
  var blueviolet: Double           = js.native
  var brown: Double                = js.native
  var burlywood: Double            = js.native
  var cadetblue: Double            = js.native
  var chartreuse: Double           = js.native
  var chocolate: Double            = js.native
  var coral: Double                = js.native
  var cornflowerblue: Double       = js.native
  var cornsilk: Double             = js.native
  var crimson: Double              = js.native
  var cyan: Double                 = js.native
  var darkblue: Double             = js.native
  var darkcyan: Double             = js.native
  var darkgoldenrod: Double        = js.native
  var darkgray: Double             = js.native
  var darkgreen: Double            = js.native
  var darkgrey: Double             = js.native
  var darkkhaki: Double            = js.native
  var darkmagenta: Double          = js.native
  var darkolivegreen: Double       = js.native
  var darkorange: Double           = js.native
  var darkorchid: Double           = js.native
  var darkred: Double              = js.native
  var darksalmon: Double           = js.native
  var darkseagreen: Double         = js.native
  var darkslateblue: Double        = js.native
  var darkslategray: Double        = js.native
  var darkslategrey: Double        = js.native
  var darkturquoise: Double        = js.native
  var darkviolet: Double           = js.native
  var deeppink: Double             = js.native
  var deepskyblue: Double          = js.native
  var dimgray: Double              = js.native
  var dimgrey: Double              = js.native
  var dodgerblue: Double           = js.native
  var firebrick: Double            = js.native
  var floralwhite: Double          = js.native
  var forestgreen: Double          = js.native
  var fuchsia: Double              = js.native
  var gainsboro: Double            = js.native
  var ghostwhite: Double           = js.native
  var gold: Double                 = js.native
  var goldenrod: Double            = js.native
  var gray: Double                 = js.native
  var green: Double                = js.native
  var greenyellow: Double          = js.native
  var grey: Double                 = js.native
  var honeydew: Double             = js.native
  var hotpink: Double              = js.native
  var indianred: Double            = js.native
  var indigo: Double               = js.native
  var ivory: Double                = js.native
  var khaki: Double                = js.native
  var lavender: Double             = js.native
  var lavenderblush: Double        = js.native
  var lawngreen: Double            = js.native
  var lemonchiffon: Double         = js.native
  var lightblue: Double            = js.native
  var lightcoral: Double           = js.native
  var lightcyan: Double            = js.native
  var lightgoldenrodyellow: Double = js.native
  var lightgray: Double            = js.native
  var lightgreen: Double           = js.native
  var lightgrey: Double            = js.native
  var lightpink: Double            = js.native
  var lightsalmon: Double          = js.native
  var lightseagreen: Double        = js.native
  var lightskyblue: Double         = js.native
  var lightslategray: Double       = js.native
  var lightslategrey: Double       = js.native
  var lightsteelblue: Double       = js.native
  var lightyellow: Double          = js.native
  var lime: Double                 = js.native
  var limegreen: Double            = js.native
  var linen: Double                = js.native
  var magenta: Double              = js.native
  var maroon: Double               = js.native
  var mediumaquamarine: Double     = js.native
  var mediumblue: Double           = js.native
  var mediumorchid: Double         = js.native
  var mediumpurple: Double         = js.native
  var mediumseagreen: Double       = js.native
  var mediumslateblue: Double      = js.native
  var mediumspringgreen: Double    = js.native
  var mediumturquoise: Double      = js.native
  var mediumvioletred: Double      = js.native
  var midnightblue: Double         = js.native
  var mintcream: Double            = js.native
  var mistyrose: Double            = js.native
  var moccasin: Double             = js.native
  var navajowhite: Double          = js.native
  var navy: Double                 = js.native
  var oldlace: Double              = js.native
  var olive: Double                = js.native
  var olivedrab: Double            = js.native
  var orange: Double               = js.native
  var orangered: Double            = js.native
  var orchid: Double               = js.native
  var palegoldenrod: Double        = js.native
  var palegreen: Double            = js.native
  var paleturquoise: Double        = js.native
  var palevioletred: Double        = js.native
  var papayawhip: Double           = js.native
  var peachpuff: Double            = js.native
  var peru: Double                 = js.native
  var pink: Double                 = js.native
  var plum: Double                 = js.native
  var powderblue: Double           = js.native
  var purple: Double               = js.native
  var red: Double                  = js.native
  var rosybrown: Double            = js.native
  var royalblue: Double            = js.native
  var saddlebrown: Double          = js.native
  var salmon: Double               = js.native
  var sandybrown: Double           = js.native
  var seagreen: Double             = js.native
  var seashell: Double             = js.native
  var sienna: Double               = js.native
  var silver: Double               = js.native
  var skyblue: Double              = js.native
  var slateblue: Double            = js.native
  var slategray: Double            = js.native
  var slategrey: Double            = js.native
  var snow: Double                 = js.native
  var springgreen: Double          = js.native
  var steelblue: Double            = js.native
  var tan: Double                  = js.native
  var teal: Double                 = js.native
  var thistle: Double              = js.native
  var tomato: Double               = js.native
  var turquoise: Double            = js.native
  var violet: Double               = js.native
  var wheat: Double                = js.native
  var white: Double                = js.native
  var whitesmoke: Double           = js.native
  var yellow: Double               = js.native
  var yellowgreen: Double          = js.native
}
