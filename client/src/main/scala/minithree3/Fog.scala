package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Fog")
class Fog protected () extends IFog {
  def this(hex: Double, near: Double = js.native, far: Double = js.native) = this()
  var name: String = js.native
  /**
     * Fog color.
     */
  var color: Color = js.native
  /**
     * The minimum distance to start applying fog. Objects that are less than 'near' units from the active camera won't be affected by fog.
     */
  var near: Double = js.native
  /**
     * The maximum distance at which fog stops being calculated and applied. Objects that are more than 'far' units away from the active camera won't be affected by fog.
     * Default is 1000.
     */
  var far: Double = js.native
  def toJSON(): js.Dynamic = js.native
}