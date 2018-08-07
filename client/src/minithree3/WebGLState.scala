package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("WebGLState")
class WebGLState protected () extends js.Object {
  def this(gl: js.Any, extensions: js.Any, paramThreeToGL: js.Function) = this()
  var buffers: js.Any = js.native
  def init(): Unit = js.native
  def initAttributes(): Unit = js.native
  def enableAttribute(attribute: String): Unit = js.native
  def enableAttributeAndDivisor(attribute: String, meshPerAttribute: js.Any, extension: js.Any): Unit = js.native
  def disableUnusedAttributes(): Unit = js.native
  def enable(id: String): Unit = js.native
  def disable(id: String): Unit = js.native
  def getCompressedTextureFormats(): js.Array[js.Any] = js.native
  def setBlending(blending: Double, blendEquation: Double, blendSrc: Double, blendDst: Double, blendEquationAlpha: Double, blendSrcAlpha: Double, blendDstAlpha: Double): Unit = js.native
  def setColorWrite(colorWrite: Double): Unit = js.native
  def setDepthTest(depthTest: Double): Unit = js.native
  def setDepthWrite(depthWrite: Double): Unit = js.native
  def setDepthFunc(depthFunc: js.Function): Unit = js.native
  def setStencilTest(stencilTest: Boolean): Unit = js.native
  def setStencilWrite(stencilWrite: js.Any): Unit = js.native
  def setStencilFunc(stencilFunc: js.Function, stencilRef: js.Any, stencilMask: Double): Unit = js.native
  def setStencilOp(stencilFail: js.Any, stencilZFail: js.Any, stencilZPass: js.Any): Unit = js.native
  def setFlipSided(flipSided: Double): Unit = js.native
  def setCullFace(cullFace: CullFace): Unit = js.native
  def setLineWidth(width: Double): Unit = js.native
  def setPolygonOffset(polygonoffset: Double, factor: Double, units: Double): Unit = js.native
  def setScissorTest(scissorTest: Boolean): Unit = js.native
  def getScissorTest(): Boolean = js.native
  def activeTexture(webglSlot: js.Any): Unit = js.native
  def bindTexture(webglType: js.Any, webglTexture: js.Any): Unit = js.native
  def compressedTexImage2D(): Unit = js.native
  def texImage2D(): Unit = js.native
  def clearColor(r: Double, g: Double, b: Double, a: Double): Unit = js.native
  def clearDepth(depth: Double): Unit = js.native
  def clearStencil(stencil: js.Any): Unit = js.native
  def scissor(scissor: js.Any): Unit = js.native
  def viewport(viewport: js.Any): Unit = js.native
  def reset(): Unit = js.native
}