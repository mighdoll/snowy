package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
@js.annotation.JSGlobal("WebGLRenderer")
class WebGLRenderer protected () extends Renderer {
  def this(parameters: WebGLRendererParameters = js.native) = this()
  /**
     * parameters is an optional object with properties defining the renderer's behaviour. The constructor also accepts no parameters at all. In all cases, it will assume sane defaults when parameters are missing.
     */
  /**
     * A Canvas where the renderer draws its output.
     * This is automatically created by the renderer in the constructor (if not provided already); you just need to add it to your page.
     */
  //var domElement: HTMLCanvasElement = js.native
  /**
     * The HTML5 Canvas's 'webgl' context obtained from the canvas where the renderer will draw.
     */
  var context: WebGLRenderingContext = js.native
  /**
     * Defines whether the renderer should automatically clear its output before rendering.
     */
  var autoClear: Boolean = js.native
  /**
     * If autoClear is true, defines whether the renderer should clear the color buffer. Default is true.
     */
  var autoClearColor: Boolean = js.native
  /**
     * If autoClear is true, defines whether the renderer should clear the depth buffer. Default is true.
     */
  var autoClearDepth: Boolean = js.native
  /**
     * If autoClear is true, defines whether the renderer should clear the stencil buffer. Default is true.
     */
  var autoClearStencil: Boolean = js.native
  /**
     * Defines whether the renderer should sort objects. Default is true.
     */
  var sortObjects: Boolean = js.native
  var clippingPlanes: js.Array[js.Any] = js.native
  var localClippingEnabled: Boolean = js.native
  var extensions: WebGLExtensions = js.native
  /**
     * Default is false.
     */
  var gammaInput: Boolean = js.native
  /**
     * Default is false.
     */
  var gammaOutput: Boolean = js.native
  var physicallyCorrectLights: Boolean = js.native
  var toneMapping: ToneMapping = js.native
  var toneMappingExposure: Double = js.native
  var toneMappingWhitePoint: Double = js.native
  /**
     * Default is false.
     */
  var shadowMapDebug: Boolean = js.native
  /**
     * Default is 8.
     */
  var maxMorphTargets: Double = js.native
  /**
     * Default is 4.
     */
  var maxMorphNormals: Double = js.native
  /**
     * An object with a series of statistical information about the graphics board memory and the rendering process. Useful for debugging or just for the sake of curiosity. The object contains the following fields:
     */
  var info: js.Any = js.native
  var shadowMap: WebGLShadowMap = js.native
  var pixelRation: Double = js.native
  var capabilities: WebGLCapabilities = js.native
  var properties: WebGLProperties = js.native
  var renderLists: WebGLRenderLists = js.native
  var state: WebGLState = js.native
  var allocTextureUnit: js.Any = js.native
  var vr: WebVRManager = js.native
  /**
     * Return the WebGL context.
     */
  def getContext(): WebGLRenderingContext = js.native
  def getContextAttributes(): js.Dynamic = js.native
  def forceContextLoss(): Unit = js.native
  /**
     * @deprecated Use { WebGLCapabilities#getMaxAnisotropy .capabilities.getMaxAnisotropy()} instead.
     */
  def getMaxAnisotropy(): Double = js.native
  /**
     * @deprecated Use { WebGLCapabilities#precision .capabilities.precision} instead.
     */
  def getPrecision(): String = js.native
  def getPixelRatio(): Double = js.native
  def setPixelRatio(value: Double): Unit = js.native
  def getSize(): js.Any = js.native
  /**
     * Resizes the output canvas to (width, height), and also sets the viewport to fit that size, starting in (0, 0).
     */
  //def setSize(width: Double, height: Double, updateStyle: Boolean = js.native): Unit = js.native
  /**
     * Sets the viewport to render from (x, y) to (x + width, y + height).
     */
  def setViewport(x: Double = js.native, y: Double = js.native, width: Double = js.native, height: Double = js.native): Unit = js.native
  /**
     * Sets the scissor area from (x, y) to (x + width, y + height).
     */
  def setScissor(x: Double, y: Double, width: Double, height: Double): Unit = js.native
  /**
     * Enable the scissor test. When this is enabled, only the pixels within the defined scissor area will be affected by further renderer actions.
     */
  def setScissorTest(enable: Boolean): Unit = js.native
  /**
     * Returns a THREE.Color instance with the current clear color.
     */
  def getClearColor(): Color = js.native
  /**
     * Sets the clear color, using color for the color and alpha for the opacity.
     */
  def setClearColor(color: Color, alpha: Double): Unit = js.native
  def setClearColor(color: String, alpha: Double): Unit = js.native
  def setClearColor(color: Double, alpha: Double): Unit = js.native
  /**
     * Returns a float with the current clear alpha. Ranges from 0 to 1.
     */
  def getClearAlpha(): Double = js.native
  def setClearAlpha(alpha: Double): Unit = js.native
  /**
     * Tells the renderer to clear its color, depth or stencil drawing buffer(s).
     * Arguments default to true
     */
  def clear(color: Boolean = js.native, depth: Boolean = js.native, stencil: Boolean = js.native): Unit = js.native
  def clearColor(): Unit = js.native
  def clearDepth(): Unit = js.native
  def clearStencil(): Unit = js.native
  def clearTarget(renderTarget: WebGLRenderTarget, color: Boolean, depth: Boolean, stencil: Boolean): Unit = js.native
  /**
     * @deprecated Use { WebGLState#reset .state.reset()} instead.
     */
  def resetGLState(): Unit = js.native
  def dispose(): Unit = js.native
  /**
     * Tells the shadow map plugin to update using the passed scene and camera parameters.
     *
     * @param scene an instance of Scene
     * @param camera — an instance of Camera
     */
  def renderBufferImmediate(`object`: Object3D, program: Object, material: Material): Unit = js.native
  def renderBufferDirect(camera: Camera, fog: Fog, material: Material, geometryGroup: js.Any, `object`: Object3D): Unit = js.native
  /**
     * Render a scene using a camera.
     * The render is done to the renderTarget (if specified) or to the canvas as usual.
     * If forceClear is true, the canvas will be cleared before rendering, even if the renderer's autoClear property is false.
     */
  def render(scene: Scene, camera: Camera, renderTarget: RenderTarget = js.native, forceClear: Boolean = js.native): Unit = js.native
  /**
     * @deprecated
     */
  def setTexture(texture: Texture, slot: Double): Unit = js.native
  def setTexture2D(texture: Texture, slot: Double): Unit = js.native
  def setTextureCube(texture: Texture, slot: Double): Unit = js.native
  def getRenderTarget(): RenderTarget = js.native
  /**
     * @deprecated Use { WebGLRenderer#getRenderTarget .getRenderTarget()} instead.
     */
  def getCurrentRenderTarget(): RenderTarget = js.native
  def setRenderTarget(renderTarget: RenderTarget = js.native): Unit = js.native
  def readRenderTargetPixels(renderTarget: RenderTarget, x: Double, y: Double, width: Double, height: Double, buffer: js.Any): Unit = js.native
  /**
     * @deprecated
     */
  var gammaFactor: Double = js.native
  /**
     * @deprecated Use { WebGLShadowMap#enabled .shadowMap.enabled} instead.
     */
  var shadowMapEnabled: Boolean = js.native
  /**
     * @deprecated Use { WebGLShadowMap#type .shadowMap.type} instead.
     */
  var shadowMapType: ShadowMapType = js.native
  /**
     * @deprecated Use { WebGLShadowMap#cullFace .shadowMap.cullFace} instead.
     */
  var shadowMapCullFace: CullFace = js.native
  /**
     * @deprecated Use { WebGLExtensions#get .extensions.get( 'OES_texture_float' )} instead.
     */
  def supportsFloatTextures(): js.Dynamic = js.native
  /**
     * @deprecated Use { WebGLExtensions#get .extensions.get( 'OES_texture_half_float' )} instead.
     */
  def supportsHalfFloatTextures(): js.Dynamic = js.native
  /**
     * @deprecated Use { WebGLExtensions#get .extensions.get( 'OES_standard_derivatives' )} instead.
     */
  def supportsStandardDerivatives(): js.Dynamic = js.native
  /**
     * @deprecated Use { WebGLExtensions#get .extensions.get( 'WEBGL_compressed_texture_s3tc' )} instead.
     */
  def supportsCompressedTextureS3TC(): js.Dynamic = js.native
  /**
     * @deprecated Use { WebGLExtensions#get .extensions.get( 'WEBGL_compressed_texture_pvrtc' )} instead.
     */
  def supportsCompressedTexturePVRTC(): js.Dynamic = js.native
  /**
     * @deprecated Use { WebGLExtensions#get .extensions.get( 'EXT_blend_minmax' )} instead.
     */
  def supportsBlendMinMax(): js.Dynamic = js.native
  /**
     * @deprecated Use { WebGLCapabilities#vertexTextures .capabilities.vertexTextures} instead.
     */
  def supportsVertexTextures(): js.Dynamic = js.native
  /**
     * @deprecated Use { WebGLExtensions#get .extensions.get( 'ANGLE_instanced_arrays' )} instead.
     */
  def supportsInstancedArrays(): js.Dynamic = js.native
  /**
     * @deprecated Use { WebGLRenderer#setScissorTest .setScissorTest()} instead.
     */
  def enableScissorTest(boolean: js.Any): js.Dynamic = js.native
}