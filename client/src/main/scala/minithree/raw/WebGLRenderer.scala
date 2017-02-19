package minithree.raw

import org.scalajs.dom.raw.HTMLCanvasElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
class WebGLRendererParameters extends js.Object {
  var canvas: HTMLCanvasElement      = js.native
  var precision: String              = js.native
  var alpha: Boolean                 = js.native
  var premultipliedAlpha: Boolean    = js.native
  var antialias: Boolean             = js.native
  var stencil: Boolean               = js.native
  var preserveDrawingBuffer: Boolean = js.native
  var clearColor: Double             = js.native
  var clearAlpha: Double             = js.native
  var devicePixelRatio: Double       = js.native
}

@js.native
@JSName("THREE.WebGLRenderer")
class WebGLRenderer(parameters: WebGLRendererParameters = js.native) extends Renderer {

  var context: js.Any                             = js.native
  var devicePixelRatio: Double                    = js.native
  var autoClear: Boolean                          = js.native
  var autoClearColor: Boolean                     = js.native
  var autoClearDepth: Boolean                     = js.native
  var autoClearStencil: Boolean                   = js.native
  var sortObjects: Boolean                        = js.native
  var gammaInput: Boolean                         = js.native
  var gammaOutput: Boolean                        = js.native
  var shadowMapEnabled: Boolean                   = js.native
  var shadowMapAutoUpdate: Boolean                = js.native
  var shadowMapType: ShadowMapType                = js.native
  var shadowMapCullFace: CullFace                 = js.native
  var shadowMapDebug: Boolean                     = js.native
  var shadowMapCascade: Boolean                   = js.native
  var maxMorphTargets: Double                     = js.native
  var maxMorphNormals: Double                     = js.native
  var autoScaleCubemaps: Boolean                  = js.native
  var renderPluginsPre: js.Array[RendererPlugin]  = js.native
  var renderPluginsPost: js.Array[RendererPlugin] = js.native
  var info: js.Any                                = js.native
  var shadowMapPlugin: ShadowMapPlugin            = js.native
  def getContext(): WebGLRenderingContext         = js.native
  def supportsVertexTextures(): Boolean           = js.native
  def supportsFloatTextures(): Boolean            = js.native
  def supportsStandardDerivatives(): Boolean      = js.native
  def supportsCompressedTextureS3TC(): Boolean    = js.native
  def getMaxAnisotropy(): Double                  = js.native
  def getPrecision(): String                      = js.native
  override def setSize(width: Double,
                       height: Double,
                       updateStyle: Boolean = js.native): Unit = js.native
  def setViewport(x: Double = js.native,
                  y: Double = js.native,
                  width: Double = js.native,
                  height: Double = js.native): Unit                         = js.native
  def setScissor(x: Double, y: Double, width: Double, height: Double): Unit = js.native
  def enableScissorTest(enable: Boolean): Unit                              = js.native
  def setClearColor(color: Color, alpha: Double = js.native): Unit          = js.native
  def setClearColorHex(hex: Double, alpha: Double): Unit                    = js.native
  def getClearColor(): Color                                                = js.native
  def getClearAlpha(): Double                                               = js.native
  def clear(color: Boolean = js.native,
            depth: Boolean = js.native,
            stencil: Boolean = js.native): Unit = js.native
  def clearColor(): Unit                        = js.native
  def clearDepth(): Unit                        = js.native
  def clearStencil(): Unit                      = js.native
  def clearTarget(renderTarget: WebGLRenderTarget,
                  color: Boolean,
                  depth: Boolean,
                  stencil: Boolean): Unit                 = js.native
  def addPostPlugin(plugin: RendererPlugin): Unit         = js.native
  def addPrePlugin(plugin: RendererPlugin): Unit          = js.native
  def updateShadowMap(scene: Scene, camera: Camera): Unit = js.native
  def renderBufferImmediate(`object`: Object3D,
                            program: Object,
                            material: Material): Unit = js.native
  def renderBufferDirect(camera: Camera,
                         lights: js.Array[Light],
                         fog: Fog,
                         material: Material,
                         geometryGroup: js.Any,
                         `object`: Object3D): Unit = js.native
  def renderBuffer(camera: Camera,
                   lights: js.Array[Light],
                   fog: Fog,
                   material: Material,
                   geometryGroup: js.Any,
                   `object`: Object3D): Unit = js.native
  def render(scene: Scene,
             camera: Camera,
             renderTarget: RenderTarget = js.native,
             forceClear: Boolean = js.native): Unit = js.native
  def renderImmediateObject(camera: Camera,
                            lights: js.Array[Light],
                            fog: Fog,
                            material: Material,
                            `object`: Object3D): Unit = js.native
  def initMaterial(material: Material,
                   lights: js.Array[Light],
                   fog: Fog,
                   `object`: Object3D): Unit = js.native
  def setFaceCulling(cullFace: CullFace = js.native,
                     frontFace: FrontFaceDirection = js.native): Unit = js.native
  def setMaterialFaces(material: Material): Unit                      = js.native
  def setDepthTest(depthTest: Boolean): Unit                          = js.native
  def setDepthWrite(depthWrite: Boolean): Unit                        = js.native
  def setBlending(blending: Blending,
                  blendEquation: BlendingEquation,
                  blendSrc: BlendingSrcFactor,
                  blendDst: BlendingDstFactor): Unit    = js.native
  def setTexture(texture: Texture, slot: Double): Unit  = js.native
  def setRenderTarget(renderTarget: RenderTarget): Unit = js.native
}
