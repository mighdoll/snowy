package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("LensFlare")
class LensFlare protected () extends Object3D {
  def this(texture: Texture = js.native, size: Double = js.native, distance: Double = js.native, blending: Blending = js.native, color: Color = js.native) = this()
  var lensFlares: js.Array[LensFlareProperty] = js.native
  var positionScreen: Vector3 = js.native
  var customUpdateCallback: js.Function1[LensFlare, Unit] = js.native
  def add(`object`: Object3D): Unit = js.native
  def add(texture: Texture, size: Double, distance: Double, blending: Blending, color: Color): Unit = js.native
  def updateLensFlares(): Unit = js.native
}