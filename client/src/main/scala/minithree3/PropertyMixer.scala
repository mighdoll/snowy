package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("PropertyMixer")
class PropertyMixer protected () extends js.Object {
  def this(binding: js.Any, typeName: String, valueSize: Double) = this()
  var binding: js.Any = js.native
  var valueSize: Double = js.native
  var buffer: js.Any = js.native
  var cumulativeWeight: Double = js.native
  var useCount: Double = js.native
  var referenceCount: Double = js.native
  def accumulate(accuIndex: Double, weight: Double): Unit = js.native
  @js.annotation.JSName("apply")
  def apply(accuIndex: Double): Unit = js.native
  def saveOriginalState(): Unit = js.native
  def restoreOriginalState(): Unit = js.native
}