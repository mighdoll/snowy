package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("OrthographicCamera")
class OrthographicCamera protected () extends Camera {
  def this(left: Double, right: Double, top: Double, bottom: Double, near: Double = js.native, far: Double = js.native) = this()
  /**
     * @param left Camera frustum left plane.
     * @param right Camera frustum right plane.
     * @param top Camera frustum top plane.
     * @param bottom Camera frustum bottom plane.
     * @param near Camera frustum near plane.
     * @param far Camera frustum far plane.
     */
  var zoom: Double = js.native
  var view: js.Any = js.native
  /**
     * Camera frustum left plane.
     */
  var left: Double = js.native
  /**
     * Camera frustum right plane.
     */
  var right: Double = js.native
  /**
     * Camera frustum top plane.
     */
  var top: Double = js.native
  /**
     * Camera frustum bottom plane.
     */
  var bottom: Double = js.native
  /**
     * Camera frustum near plane.
     */
  var near: Double = js.native
  /**
     * Camera frustum far plane.
     */
  var far: Double = js.native
  /**
     * Updates the camera projection matrix. Must be called after change of parameters.
     */
  def updateProjectionMatrix(): Unit = js.native
  def setViewOffset(fullWidth: Double, fullHeight: Double, offsetX: Double, offsetY: Double, width: Double, height: Double): Unit = js.native
  def clearViewOffset(): Unit = js.native
  def toJSON(meta: js.Any = js.native): js.Dynamic = js.native
}