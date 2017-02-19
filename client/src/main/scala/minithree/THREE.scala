package minithree

import minithree.raw._

import scala.scalajs.js

@js.native
object jsThree extends js.Object {
  var REVISION: String                               = js.native
  var CullFaceNone: CullFace                         = js.native
  var CullFaceBack: CullFace                         = js.native
  var CullFaceFront: CullFace                        = js.native
  var CullFaceFrontBack: CullFace                    = js.native
  var FrontFaceDirectionCW: FrontFaceDirection       = js.native
  var FrontFaceDirectionCCW: FrontFaceDirection      = js.native
  var BasicShadowMap: ShadowMapType                  = js.native
  var PCFShadowMap: ShadowMapType                    = js.native
  var PCFSoftShadowMap: ShadowMapType                = js.native
  var FrontSide: Side                                = js.native
  var BackSide: Side                                 = js.native
  var DoubleSide: Side                               = js.native
  var NoShading: Shading                             = js.native
  var FlatShading: Shading                           = js.native
  var SmoothShading: Shading                         = js.native
  var NoColors: Colors                               = js.native
  var FaceColors: Colors                             = js.native
  var VertexColors: Colors                           = js.native
  var NoBlending: Blending                           = js.native
  var NormalBlending: Blending                       = js.native
  var AdditiveBlending: Blending                     = js.native
  var SubtractiveBlending: Blending                  = js.native
  var MultiplyBlending: Blending                     = js.native
  var CustomBlending: Blending                       = js.native
  var AddEquation: BlendingEquation                  = js.native
  var SubtractEquation: BlendingEquation             = js.native
  var ReverseSubtractEquation: BlendingEquation      = js.native
  var ZeroFactor: BlendingDstFactor                  = js.native
  var OneFactor: BlendingDstFactor                   = js.native
  var SrcColorFactor: BlendingDstFactor              = js.native
  var OneMinusSrcColorFactor: BlendingDstFactor      = js.native
  var SrcAlphaFactor: BlendingDstFactor              = js.native
  var OneMinusSrcAlphaFactor: BlendingDstFactor      = js.native
  var DstAlphaFactor: BlendingDstFactor              = js.native
  var OneMinusDstAlphaFactor: BlendingDstFactor      = js.native
  var DstColorFactor: BlendingSrcFactor              = js.native
  var OneMinusDstColorFactor: BlendingSrcFactor      = js.native
  var SrcAlphaSaturateFactor: BlendingSrcFactor      = js.native
  var MultiplyOperation: Combine                     = js.native
  var MixOperation: Combine                          = js.native
  var AddOperation: Combine                          = js.native
  var UVMapping: MappingConstructor                  = js.native
  var CubeReflectionMapping: MappingConstructor      = js.native
  var CubeRefractionMapping: MappingConstructor      = js.native
  var SphericalReflectionMapping: MappingConstructor = js.native
  var SphericalRefractionMapping: MappingConstructor = js.native
  var RepeatWrapping: Wrapping                       = js.native
  var ClampToEdgeWrapping: Wrapping                  = js.native
  var MirroredRepeatWrapping: Wrapping               = js.native
  var NearestFilter: TextureFilter                   = js.native
  var NearestMipMapNearestFilter: TextureFilter      = js.native
  var NearestMipMapLinearFilter: TextureFilter       = js.native
  var LinearFilter: TextureFilter                    = js.native
  var LinearMipMapNearestFilter: TextureFilter       = js.native
  var LinearMipMapLinearFilter: TextureFilter        = js.native
  var UnsignedByteType: TextureDataType              = js.native
  var ByteType: TextureDataType                      = js.native
  var ShortType: TextureDataType                     = js.native
  var UnsignedShortType: TextureDataType             = js.native
  var IntType: TextureDataType                       = js.native
  var UnsignedIntType: TextureDataType               = js.native
  var FloatType: TextureDataType                     = js.native
  var UnsignedShort4444Type: PixelType               = js.native
  var UnsignedShort5551Type: PixelType               = js.native
  var UnsignedShort565Type: PixelType                = js.native
  var AlphaFormat: PixelFormat                       = js.native
  var RGBFormat: PixelFormat                         = js.native
  var RGBAFormat: PixelFormat                        = js.native
  var LuminanceFormat: PixelFormat                   = js.native
  var LuminanceAlphaFormat: PixelFormat              = js.native
  var RGB_S3TC_DXT1_Format: CompressedPixelFormat    = js.native
  var RGBA_S3TC_DXT1_Format: CompressedPixelFormat   = js.native
  var RGBA_S3TC_DXT3_Format: CompressedPixelFormat   = js.native
  var RGBA_S3TC_DXT5_Format: CompressedPixelFormat   = js.native
  var Math: Math                                     = js.native
  var LineStrip: LineType                            = js.native
  var LinePieces: LineType                           = js.native
  var ShaderChunk: ShaderChunk                       = js.native
}
object THREE {
  @inline def REVISION: String                          = jsThree.REVISION
  @inline def CullFaceNone: CullFace                    = jsThree.CullFaceNone
  @inline def CullFaceBack: CullFace                    = jsThree.CullFaceBack
  @inline def CullFaceFront: CullFace                   = jsThree.CullFaceFront
  @inline def CullFaceFrontBack: CullFace               = jsThree.CullFaceFrontBack
  @inline def FrontFaceDirectionCW: FrontFaceDirection  = jsThree.FrontFaceDirectionCW
  @inline def FrontFaceDirectionCCW: FrontFaceDirection = jsThree.FrontFaceDirectionCCW
  @inline def BasicShadowMap: ShadowMapType             = jsThree.BasicShadowMap
  @inline def PCFShadowMap: ShadowMapType               = jsThree.PCFShadowMap
  @inline def PCFSoftShadowMap: ShadowMapType           = jsThree.PCFSoftShadowMap
  @inline def FrontSide: Side                           = jsThree.FrontSide
  @inline def BackSide: Side                            = jsThree.BackSide
  @inline def DoubleSide: Side                          = jsThree.DoubleSide
  @inline def NoShading: Shading                        = jsThree.NoShading
  @inline def FlatShading: Shading                      = jsThree.FlatShading
  @inline def SmoothShading: Shading                    = jsThree.SmoothShading
  @inline def NoColors: Colors                          = jsThree.NoColors
  @inline def FaceColors: Colors                        = jsThree.FaceColors
  @inline def VertexColors: Colors                      = jsThree.VertexColors
  @inline def NoBlending: Blending                      = jsThree.NoBlending
  @inline def NormalBlending: Blending                  = jsThree.NormalBlending
  @inline def AdditiveBlending: Blending                = jsThree.AdditiveBlending
  @inline def SubtractiveBlending: Blending             = jsThree.SubtractiveBlending
  @inline def MultiplyBlending: Blending                = jsThree.MultiplyBlending
  @inline def CustomBlending: Blending                  = jsThree.CustomBlending
  @inline def AddEquation: BlendingEquation             = jsThree.AddEquation
  @inline def SubtractEquation: BlendingEquation        = jsThree.SubtractEquation
  @inline def ReverseSubtractEquation: BlendingEquation = jsThree.ReverseSubtractEquation
  @inline def ZeroFactor: BlendingDstFactor             = jsThree.ZeroFactor
  @inline def OneFactor: BlendingDstFactor              = jsThree.OneFactor
  @inline def SrcColorFactor: BlendingDstFactor         = jsThree.SrcColorFactor
  @inline def OneMinusSrcColorFactor: BlendingDstFactor = jsThree.OneMinusSrcColorFactor
  @inline def SrcAlphaFactor: BlendingDstFactor         = jsThree.SrcAlphaFactor
  @inline def OneMinusSrcAlphaFactor: BlendingDstFactor = jsThree.OneMinusSrcAlphaFactor
  @inline def DstAlphaFactor: BlendingDstFactor         = jsThree.DstAlphaFactor
  @inline def OneMinusDstAlphaFactor: BlendingDstFactor = jsThree.OneMinusDstAlphaFactor
  @inline def DstColorFactor: BlendingSrcFactor         = jsThree.DstColorFactor
  @inline def OneMinusDstColorFactor: BlendingSrcFactor = jsThree.OneMinusDstColorFactor
  @inline def SrcAlphaSaturateFactor: BlendingSrcFactor = jsThree.SrcAlphaSaturateFactor
  @inline def MultiplyOperation: Combine                = jsThree.MultiplyOperation
  @inline def MixOperation: Combine                     = jsThree.MixOperation
  @inline def AddOperation: Combine                     = jsThree.AddOperation
  @inline def UVMapping: MappingConstructor             = jsThree.UVMapping
  @inline def CubeReflectionMapping: MappingConstructor = jsThree.CubeReflectionMapping
  @inline def CubeRefractionMapping: MappingConstructor = jsThree.CubeRefractionMapping
  @inline def SphericalReflectionMapping: MappingConstructor =
    jsThree.SphericalReflectionMapping
  @inline def SphericalRefractionMapping: MappingConstructor =
    jsThree.SphericalRefractionMapping
  @inline def RepeatWrapping: Wrapping         = jsThree.RepeatWrapping
  @inline def ClampToEdgeWrapping: Wrapping    = jsThree.ClampToEdgeWrapping
  @inline def MirroredRepeatWrapping: Wrapping = jsThree.MirroredRepeatWrapping
  @inline def NearestFilter: TextureFilter     = jsThree.NearestFilter
  @inline def NearestMipMapNearestFilter: TextureFilter =
    jsThree.NearestMipMapNearestFilter
  @inline def NearestMipMapLinearFilter: TextureFilter =
    jsThree.NearestMipMapLinearFilter
  @inline def LinearFilter: TextureFilter = jsThree.LinearFilter
  @inline def LinearMipMapNearestFilter: TextureFilter =
    jsThree.LinearMipMapNearestFilter
  @inline def LinearMipMapLinearFilter: TextureFilter     = jsThree.LinearMipMapLinearFilter
  @inline def UnsignedByteType: TextureDataType           = jsThree.UnsignedByteType
  @inline def ByteType: TextureDataType                   = jsThree.ByteType
  @inline def ShortType: TextureDataType                  = jsThree.ShortType
  @inline def UnsignedShortType: TextureDataType          = jsThree.UnsignedShortType
  @inline def IntType: TextureDataType                    = jsThree.IntType
  @inline def UnsignedIntType: TextureDataType            = jsThree.UnsignedIntType
  @inline def FloatType: TextureDataType                  = jsThree.FloatType
  @inline def UnsignedShort4444Type: PixelType            = jsThree.UnsignedShort4444Type
  @inline def UnsignedShort5551Type: PixelType            = jsThree.UnsignedShort5551Type
  @inline def UnsignedShort565Type: PixelType             = jsThree.UnsignedShort565Type
  @inline def AlphaFormat: PixelFormat                    = jsThree.AlphaFormat
  @inline def RGBFormat: PixelFormat                      = jsThree.RGBFormat
  @inline def RGBAFormat: PixelFormat                     = jsThree.RGBAFormat
  @inline def LuminanceFormat: PixelFormat                = jsThree.LuminanceFormat
  @inline def LuminanceAlphaFormat: PixelFormat           = jsThree.LuminanceAlphaFormat
  @inline def RGB_S3TC_DXT1_Format: CompressedPixelFormat = jsThree.RGBA_S3TC_DXT1_Format
  @inline def RGBA_S3TC_DXT1_Format: CompressedPixelFormat =
    jsThree.RGBA_S3TC_DXT3_Format
  @inline def RGBA_S3TC_DXT3_Format: CompressedPixelFormat =
    jsThree.RGBA_S3TC_DXT5_Format
  @inline def RGBA_S3TC_DXT5_Format: CompressedPixelFormat =
    jsThree.RGBA_S3TC_DXT5_Format
  @inline def Math: Math               = jsThree.Math
  @inline def LineStrip: LineType      = jsThree.LineStrip
  @inline def LinePieces: LineType     = jsThree.LinePieces
  @inline def ShaderChunk: ShaderChunk = jsThree.ShaderChunk

  type Blending = raw.Blending
  @inline def Blending = raw.Blending
  type BlendingDstFactor = raw.BlendingDstFactor
  @inline def BlendingDstFactor = raw.BlendingDstFactor
  type BlendingEquation = raw.BlendingEquation
  @inline def BlendingEquation = raw.BlendingEquation
  type BlendingSrcFactor = raw.BlendingSrcFactor
  @inline def BlendingSrcFactor = raw.BlendingSrcFactor

  type Box3 = raw.Box3

  type Camera = raw.Camera

  type Color = raw.Color
  type HSL   = raw.HSL

  type CullFace = raw.CullFace
  @inline def CullFace = raw.CullFace

  type EventDispatcher = raw.EventDispatcher

  type Face3 = raw.Face3

  type Fog     = raw.Fog
  type FogExp2 = raw.FogExp2
  type IFog    = raw.IFog

  type FrontFaceDirection = raw.FrontFaceDirection
  @inline def FrontFaceDirection = raw.FrontFaceDirection

  type Intersection = raw.Intersection

  type Light            = raw.Light
  type AmbientLight     = raw.AmbientLight
  type AreaLight        = raw.AreaLight
  type DirectionalLight = raw.DirectionalLight
  type HemisphereLight  = raw.HemisphereLight
  type PointLight       = raw.PointLight
  type SpotLight        = raw.SpotLight

  type Line3 = raw.Line3

  type Mapping = raw.Mapping
  @inline def Mapping = raw.Mapping
  type MappingConstructor = raw.MappingConstructor

  type Material = raw.Material

  type Euler      = raw.Euler
  type Quaternion = raw.Quaternion
  type Spherical  = raw.Spherical

  type Matrix  = raw.Matrix
  type Matrix3 = raw.Matrix3
  type Matrix4 = raw.Matrix4

  type Object3D = raw.Object3D

  type PixelFormat = raw.PixelFormat
  @inline def PixelFormat = raw.PixelFormat
  type PixelType = raw.PixelType
  @inline def PixelType = raw.PixelType

  type Plane = raw.Plane

  type Ray = raw.Ray

  type Raycaster           = raw.Raycaster
  type RaycasterParameters = raw.RaycasterParameters

  type Renderer       = raw.Renderer
  type RendererPlugin = raw.RendererPlugin
  type RendererTarget = raw.RenderTarget

  type Scene = raw.Scene

  type ShadowMapPlugin = raw.ShadowMapPlugin
  type ShadowMapType   = raw.ShadowMapType
  @inline def ShadowMapType = raw.ShadowMapType

  type Side = raw.Side
  @inline def Side = raw.Side

  type Sphere = raw.Sphere

  type Texture = raw.Texture
  @inline def Texture = raw.Texture
  type TextureDataType = raw.TextureDataType
  @inline def TextureDataType = raw.TextureDataType
  type TextureFilter = raw.TextureFilter
  @inline def TextureFilter = raw.TextureFilter

  type Vector  = raw.Vector
  type Vector2 = raw.Vector2
  type Vector3 = raw.Vector3

  type WebGLRenderer           = raw.WebGLRenderer
  type WebGLRendererParameters = raw.WebGLRendererParameters

  type WebGLRenderingContext    = raw.WebGLRenderingContext
  type WebGLRenderTarget        = raw.WebGLRenderTarget
  type WebGLRenderTargetOptions = raw.WebGLRenderTargetOptions

  type Wrapping = raw.Wrapping
  @inline def Wrapping = raw.Wrapping
}
