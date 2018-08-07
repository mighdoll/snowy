package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Loader")
class Loader extends js.Object {
  /**
     * Will be called when load starts.
     * The default is a function with empty body.
     */
  var onLoadStart: js.Function0[Unit] = js.native
  /**
     * Will be called while load progresses.
     * The default is a function with empty body.
     */
  var onLoadProgress: js.Function0[Unit] = js.native
  /**
     * Will be called when load completes.
     * The default is a function with empty body.
     */
  var onLoadComplete: js.Function0[Unit] = js.native
  /**
     * default — null.
     * If set, assigns the crossOrigin attribute of the image to the value of crossOrigin, prior to starting the load.
     */
  var crossOrigin: String = js.native
  /**
     * @deprecated Use THREE.LoaderUtils.extractUrlBase() instead.
     */
  def extractUrlBase(url: String): String = js.native
  def initMaterials(materials: js.Array[Material], texturePath: String): js.Array[Material] = js.native
  def createMaterial(m: Material, texturePath: String, crossOrigin: String = js.native): Boolean = js.native
}

@js.native
@js.annotation.JSGlobal("Loader")
object Loader extends js.Object {
  var Handlers: LoaderHandler = js.native
}