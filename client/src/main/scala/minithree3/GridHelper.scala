package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("GridHelper")
class GridHelper protected () extends LineSegments {
  def this(size: Double, divisions: Double, color1: Color | Double = js.native, color2: Color | Double = js.native) = this()
  /**
     * @deprecated Colors should be specified in the constructor.
     */
  def setColors(color1: Color | Double = js.native, color2: Color | Double = js.native): Unit = js.native
}