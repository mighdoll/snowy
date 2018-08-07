package minithree3.AnimationUtils

import scala.scalajs.js
@js.native
@js.annotation.JSGlobal("AnimationUtils")
object AnimationUtils extends js.Object {
  def arraySlice(array: js.Any, from: Double, to: Double): js.Dynamic = js.native
  def convertArray(array: js.Any, `type`: js.Any, forceClone: Boolean): js.Dynamic = js.native
  def isTypedArray(`object`: js.Any): Boolean = js.native
  def getKeyFrameOrder(times: Double): js.Array[Double] = js.native
  def sortedArray(values: js.Array[js.Any], stride: Double, order: js.Array[Double]): js.Array[js.Any] = js.native
  def flattenJSON(jsonKeys: js.Array[String], times: js.Array[js.Any], values: js.Array[js.Any], valuePropertyName: String): Unit = js.native
}