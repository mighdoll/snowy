package minithree3

import org.scalajs.dom.{ErrorEvent, ProgressEvent}

import scala.scalajs.js

@js.native
trait AnyLoader extends js.Object {
  def load(url: String, onLoad: js.Function1[js.Any, Unit] = js.native, onProgress: js.Function1[ProgressEvent, Unit] = js.native, onError: js.Function1[ErrorEvent, Unit] = js.native): js.Dynamic = js.native
}