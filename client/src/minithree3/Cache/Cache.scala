package minithree3.Cache

import scala.scalajs.js
@js.native
@js.annotation.JSGlobal("Cache")
object Cache extends js.Object {
  val enabled: Boolean = js.native
  val files: js.Any = js.native
  def add(key: String, file: js.Any): Unit = js.native
  def get(key: String): js.Dynamic = js.native
  def remove(key: String): Unit = js.native
  def clear(): Unit = js.native
}