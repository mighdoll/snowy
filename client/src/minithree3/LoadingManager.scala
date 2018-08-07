package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LoadingManager")
class LoadingManager protected () extends js.Object {
  def this(onLoad: js.Function0[Unit] = js.native, onProgress: js.Function3[String, Double, Double, Unit] = js.native, onError: js.Function0[Unit] = js.native) = this()
  var onStart: js.Function0[Unit] = js.native
  /**
     * Will be called when load starts.
     * The default is a function with empty body.
     */
  var onLoad: js.Function0[Unit] = js.native
  /**
     * Will be called while load progresses.
     * The default is a function with empty body.
     */
  var onProgress: js.Function3[js.Any, Double, Double, Unit] = js.native
  /**
     * Will be called when each element in the scene completes loading.
     * The default is a function with empty body.
     */
  var onError: js.Function0[Unit] = js.native
  def itemStart(url: String): Unit = js.native
  def itemEnd(url: String): Unit = js.native
  def itemError(url: String): Unit = js.native
}