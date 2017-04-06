package minithree2.THREE

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("THREE")
object THREE extends js.Object {
  val REVISION: String = js.native

  @js.native
  object MOUSE extends js.Object {
    val LEFT: MOUSE   = js.native
    val MIDDLE: MOUSE = js.native
    val RIGHT: MOUSE  = js.native
  }

  val LoopOnce: LoopMode     = js.native
  val LoopRepeat: LoopMode   = js.native
  val LoopPingPong: LoopMode = js.native

  val InterpolateDiscrete: InterpolationMode = js.native
  val InterpolateLinear: InterpolationMode   = js.native
  val InterpolateSmooth: InterpolationMode   = js.native

  val ZeroCurvatureEnding: EndingMode = js.native
  val ZeroSlopeEnding: EndingMode     = js.native
  val WrapAroundEnding: EndingMode    = js.native

  val AddEquation: BlendingEquation             = js.native
  val SubtractEquation: BlendingEquation        = js.native
  val ReverseSubtractEquation: BlendingEquation = js.native
  val MinEquation: BlendingEquation             = js.native
  val MaxEquation: BlendingEquation             = js.native

  val ZeroFactor: SourceFactor with DestinationFactor             = js.native
  val OneFactor: SourceFactor with DestinationFactor              = js.native
  val SrcColorFactor: SourceFactor with DestinationFactor         = js.native
  val OneMinusSrcColorFactor: SourceFactor with DestinationFactor = js.native
  val SrcAlphaFactor: SourceFactor with DestinationFactor         = js.native
  val OneMinusSrcAlphaFactor: SourceFactor with DestinationFactor = js.native
  val DstAlphaFactor: SourceFactor with DestinationFactor         = js.native
  val OneMinusDstAlphaFactor: SourceFactor with DestinationFactor = js.native
  val DstColorFactor: SourceFactor with DestinationFactor         = js.native
  val OneMinusDstColorFactor: SourceFactor with DestinationFactor = js.native
  val SrcAlphaSaturateFactor: SourceFactor                        = js.native

  val TrianglesDrawMode: DrawMode     = js.native
  val TriangleStripDrawMode: DrawMode = js.native
  val TriangleFanDrawMode: DrawMode   = js.native

  val FrontSide: Side  = js.native
  val BackSide: Side   = js.native
  val DoubleSide: Side = js.native

  val SmoothShading: Shading = js.native
  val FlatShading: Shading   = js.native

  val NoColors: Colors     = js.native
  val FaceColors: Colors   = js.native
  val VertexColors: Colors = js.native

  val NoBlending: BlendingMode          = js.native
  val NormalBlending: BlendingMode      = js.native
  val AdditiveBlending: BlendingMode    = js.native
  val SubtractiveBlending: BlendingMode = js.native
  val MultiplyBlending: BlendingMode    = js.native
  val CustomBlending: BlendingMode      = js.native

  val NeverDepth: DepthMode        = js.native
  val AlwaysDepth: DepthMode       = js.native
  val LessDepth: DepthMode         = js.native
  val LessEqualDepth: DepthMode    = js.native
  val GreaterEqualDepth: DepthMode = js.native
  val GreaterDepth: DepthMode      = js.native
  val NotEqualDepth: DepthMode     = js.native

  val MultiplyOperation: TextureCombineOperation = js.native
  val MixOperation: TextureCombineOperation      = js.native
  val AddOperation: TextureCombineOperation      = js.native

  val CullFaceNone: CullFaceMode      = js.native
  val CullFaceBack: CullFaceMode      = js.native
  val CullFaceFront: CullFaceMode     = js.native
  val CullFaceFrontBack: CullFaceMode = js.native

  val FrontFaceDirectionCW: FrontFaceDirection  = js.native
  val FrontFaceDirectionCCW: FrontFaceDirection = js.native

  val BasicShadowMap: ShadowType   = js.native
  val PCFShadowMap: ShadowType     = js.native
  val PCFSoftShadowMap: ShadowType = js.native

  val NoToneMapping: ToneMapping         = js.native
  val LinearToneMapping: ToneMapping     = js.native
  val ReinhardToneMapping: ToneMapping   = js.native
  val Uncharted2ToneMapping: ToneMapping = js.native
  val CineonToneMapping: ToneMapping     = js.native

  val UVMapping: MappingMode                        = js.native
  val CubeReflectionMapping: MappingMode            = js.native
  val CubeRefractionMapping: MappingMode            = js.native
  val EquirectangularReflectionMapping: MappingMode = js.native
  val EquirectangularRefractionMapping: MappingMode = js.native
  val SphericalReflectionMapping: MappingMode       = js.native
  val CubeUVReflectionMapping: MappingMode          = js.native
  val CubeUVRefractionMapping: MappingMode          = js.native

  val RepeatWrapping: WrappingMode         = js.native
  val ClampToEdgeWrapping: WrappingMode    = js.native
  val MirroredRepeatWrapping: WrappingMode = js.native

  val NearestFilter: MagnificationFilter with MinificationFilter = js.native
  val LinearFilter: MagnificationFilter with MinificationFilter  = js.native

  val NearestMipMapNearestFilter: MinificationFilter = js.native
  val NearestMipMapLinearFilter: MinificationFilter  = js.native
  val LinearMipMapNearestFilter: MinificationFilter  = js.native
  val LinearMipMapLinearFilter: MinificationFilter   = js.native

  val UnsignedByteType: TextureType      = js.native
  val ByteType: TextureType              = js.native
  val ShortType: TextureType             = js.native
  val UnsignedShortType: TextureType     = js.native
  val IntType: TextureType               = js.native
  val UnsignedIntType: TextureType       = js.native
  val FloatType: TextureType             = js.native
  val HalfFloatType: TextureType         = js.native
  val UnsignedShort4444Type: TextureType = js.native
  val UnsignedShort5551Type: TextureType = js.native
  val UnsignedShort565Type: TextureType  = js.native
  val UnsignedInt248Type: TextureType    = js.native

  val AlphaFormat: TextureFormat          = js.native
  val RGBFormat: TextureFormat            = js.native
  val RGBAFormat: TextureFormat           = js.native
  val LuminanceFormat: TextureFormat      = js.native
  val LuminanceAlphaFormat: TextureFormat = js.native
  val RGBEFormat: TextureFormat           = js.native
  val DepthFormat: TextureFormat          = js.native
  val DepthStencilFormat: TextureFormat   = js.native

  val RGB_S3TC_DXT1_Format: CompressedTextureFormat  = js.native
  val RGBA_S3TC_DXT1_Format: CompressedTextureFormat = js.native
  val RGBA_S3TC_DXT3_Format: CompressedTextureFormat = js.native
  val RGBA_S3TC_DXT5_Format: CompressedTextureFormat = js.native

  val RGB_PVRTC_4BPPV1_Format: CompressedTextureFormat  = js.native
  val RGB_PVRTC_2BPPV1_Format: CompressedTextureFormat  = js.native
  val RGBA_PVRTC_4BPPV1_Format: CompressedTextureFormat = js.native
  val RGBA_PVRTC_2BPPV1_Format: CompressedTextureFormat = js.native

  val RGB_ETC1_Format: CompressedTextureFormat = js.native

  val LinearEncoding: TextureEncoding    = js.native
  val sRGBEncoding: TextureEncoding      = js.native
  val GammaEncoding: TextureEncoding     = js.native
  val RGBEEncoding: TextureEncoding      = js.native
  val LogLuvEncoding: TextureEncoding    = js.native
  val RGBM7Encoding: TextureEncoding     = js.native
  val RGBM16Encoding: TextureEncoding    = js.native
  val RGBDEncoding: TextureEncoding      = js.native
  val BasicDepthPacking: TextureEncoding = js.native
  val RGBADepthPacking: TextureEncoding  = js.native
}

@js.native
sealed trait MOUSE extends js.Object

@js.native
sealed trait LoopMode extends js.Object

@js.native
sealed trait InterpolationMode extends js.Object

@js.native
sealed trait EndingMode extends js.Object

@js.native
sealed trait BlendingEquation extends js.Object

@js.native
sealed trait SourceFactor extends js.Object

@js.native
sealed trait DestinationFactor extends js.Object

@js.native
sealed trait DrawMode extends js.Object

@js.native
sealed trait Side extends js.Object

@js.native
sealed trait Shading extends js.Object

@js.native
sealed trait Colors extends js.Object

@js.native
sealed trait BlendingMode extends js.Object

@js.native
sealed trait DepthMode extends js.Object

@js.native
sealed trait TextureCombineOperation extends js.Object

@js.native
sealed trait CullFaceMode extends js.Object

@js.native
sealed trait FrontFaceDirection extends js.Object

@js.native
sealed trait ShadowType extends js.Object

@js.native
sealed trait ToneMapping extends js.Object

@js.native
sealed trait MappingMode extends js.Object

@js.native
sealed trait WrappingMode extends js.Object

@js.native
sealed trait MagnificationFilter extends js.Object

@js.native
sealed trait MinificationFilter extends js.Object

@js.native
sealed trait TextureType extends js.Object

@js.native
sealed trait TextureFormat extends js.Object

@js.native
sealed trait CompressedTextureFormat extends js.Object

@js.native
sealed trait TextureEncoding extends js.Object
