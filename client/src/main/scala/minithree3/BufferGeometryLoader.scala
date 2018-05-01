package minithree3

import scala.scalajs.js
import com.definitelyscala.node.Buffer

@js.native
@js.annotation.JSGlobal("BufferGeometryLoader")
class BufferGeometryLoader protected () extends js.Object {
  def this(manager: LoadingManager = js.native) = this()
  var manager: LoadingManager = js.native
  def load(url: String, onLoad: js.Function1[BufferGeometry, Unit], onProgress: js.Function1[js.Any, Unit] = js.native, onError: js.Function1[js.Any, Unit] = js.native): Unit = js.native
  def parse(json: js.Any): BufferGeometry = js.native
}