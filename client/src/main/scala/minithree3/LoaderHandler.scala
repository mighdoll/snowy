package minithree3

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait LoaderHandler extends js.Object {
  var handlers: js.Array[`js.RegExp` | AnyLoader] = js.native
  def add(regex: `js.RegExp`, loader: AnyLoader): Unit = js.native
  def get(file: String): AnyLoader | Null = js.native
}