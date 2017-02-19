package minithree.raw

import scala.scalajs.js

@js.native
trait EventDispatcher extends js.Object {
  def addEventListener(`type`: String, listener: js.Function1[js.Any, Unit]): Unit =
    js.native
  def hasEventListener(`type`: String, listener: js.Function1[js.Any, Unit]): Boolean =
    js.native
  def removeEventListener(`type`: String, listener: js.Function1[js.Any, Unit]): Unit =
    js.native
  def dispatchEvent(event: js.Any): Unit = js.native
}
