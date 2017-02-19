package minithree

object THREE {
  @inline def REVISION: String                         = raw.jsThree.REVISION
  @inline def CullFaceNone: CullFace                   = raw.jsThree.CullFaceNone
  @inline def CullFaceBack: CullFace                   = raw.jsThree.CullFaceBack
  @inline def CullFaceFront: CullFace                  = raw.jsThree.CullFaceFront
  @inline def CullFaceFrontBack: CullFace              = raw.jsThree.CullFaceFrontBack
  @inline def FrontFaceDirectionCW: FrontFaceDirection = raw.jsThree.FrontFaceDirectionCW
  @inline def FrontFaceDirectionCCW: FrontFaceDirection =
    raw.jsThree.FrontFaceDirectionCCW
  @inline def BasicShadowMap: ShadowMapType      = raw.jsThree.BasicShadowMap
  @inline def PCFShadowMap: ShadowMapType        = raw.jsThree.PCFShadowMap
  @inline def PCFSoftShadowMap: ShadowMapType    = raw.jsThree.PCFSoftShadowMap
  @inline def FrontSide: Side                    = raw.jsThree.FrontSide
  @inline def BackSide: Side                     = raw.jsThree.BackSide
  @inline def DoubleSide: Side                   = raw.jsThree.DoubleSide
  @inline def NoShading: Shading                 = raw.jsThree.NoShading
  @inline def FlatShading: Shading               = raw.jsThree.FlatShading
  @inline def SmoothShading: Shading             = raw.jsThree.SmoothShading
  @inline def NoColors: Colors                   = raw.jsThree.NoColors
  @inline def FaceColors: Colors                 = raw.jsThree.FaceColors
  @inline def VertexColors: Colors               = raw.jsThree.VertexColors
  @inline def NoBlending: Blending               = raw.jsThree.NoBlending
  @inline def NormalBlending: Blending           = raw.jsThree.NormalBlending
  @inline def AdditiveBlending: Blending         = raw.jsThree.AdditiveBlending
  @inline def SubtractiveBlending: Blending      = raw.jsThree.SubtractiveBlending
  @inline def MultiplyBlending: Blending         = raw.jsThree.MultiplyBlending
  @inline def CustomBlending: Blending           = raw.jsThree.CustomBlending
  @inline def AddEquation: BlendingEquation      = raw.jsThree.AddEquation
  @inline def SubtractEquation: BlendingEquation = raw.jsThree.SubtractEquation
  @inline def ReverseSubtractEquation: BlendingEquation =
    raw.jsThree.ReverseSubtractEquation
  @inline def ZeroFactor: BlendingDstFactor     = raw.jsThree.ZeroFactor
  @inline def OneFactor: BlendingDstFactor      = raw.jsThree.OneFactor
  @inline def SrcColorFactor: BlendingDstFactor = raw.jsThree.SrcColorFactor
  @inline def OneMinusSrcColorFactor: BlendingDstFactor =
    raw.jsThree.OneMinusSrcColorFactor
  @inline def SrcAlphaFactor: BlendingDstFactor = raw.jsThree.SrcAlphaFactor
  @inline def OneMinusSrcAlphaFactor: BlendingDstFactor =
    raw.jsThree.OneMinusSrcAlphaFactor
  @inline def DstAlphaFactor: BlendingDstFactor = raw.jsThree.DstAlphaFactor
  @inline def OneMinusDstAlphaFactor: BlendingDstFactor =
    raw.jsThree.OneMinusDstAlphaFactor
  @inline def DstColorFactor: BlendingSrcFactor = raw.jsThree.DstColorFactor
  @inline def OneMinusDstColorFactor: BlendingSrcFactor =
    raw.jsThree.OneMinusDstColorFactor
  @inline def SrcAlphaSaturateFactor: BlendingSrcFactor =
    raw.jsThree.SrcAlphaSaturateFactor
  @inline def MultiplyOperation: Combine    = raw.jsThree.MultiplyOperation
  @inline def MixOperation: Combine         = raw.jsThree.MixOperation
  @inline def AddOperation: Combine         = raw.jsThree.AddOperation
  @inline def UVMapping: MappingConstructor = raw.jsThree.UVMapping
  @inline def CubeReflectionMapping: MappingConstructor =
    raw.jsThree.CubeReflectionMapping
  @inline def CubeRefractionMapping: MappingConstructor =
    raw.jsThree.CubeRefractionMapping
  @inline def SphericalReflectionMapping: MappingConstructor =
    raw.jsThree.SphericalReflectionMapping
  @inline def SphericalRefractionMapping: MappingConstructor =
    raw.jsThree.SphericalRefractionMapping
  @inline def RepeatWrapping: Wrapping         = raw.jsThree.RepeatWrapping
  @inline def ClampToEdgeWrapping: Wrapping    = raw.jsThree.ClampToEdgeWrapping
  @inline def MirroredRepeatWrapping: Wrapping = raw.jsThree.MirroredRepeatWrapping
  @inline def NearestFilter: TextureFilter     = raw.jsThree.NearestFilter
  @inline def NearestMipMapNearestFilter: TextureFilter =
    raw.jsThree.NearestMipMapNearestFilter
  @inline def NearestMipMapLinearFilter: TextureFilter =
    raw.jsThree.NearestMipMapLinearFilter
  @inline def LinearFilter: TextureFilter = raw.jsThree.LinearFilter
  @inline def LinearMipMapNearestFilter: TextureFilter =
    raw.jsThree.LinearMipMapNearestFilter
  @inline def LinearMipMapLinearFilter: TextureFilter =
    raw.jsThree.LinearMipMapLinearFilter
  @inline def UnsignedByteType: TextureDataType  = raw.jsThree.UnsignedByteType
  @inline def ByteType: TextureDataType          = raw.jsThree.ByteType
  @inline def ShortType: TextureDataType         = raw.jsThree.ShortType
  @inline def UnsignedShortType: TextureDataType = raw.jsThree.UnsignedShortType
  @inline def IntType: TextureDataType           = raw.jsThree.IntType
  @inline def UnsignedIntType: TextureDataType   = raw.jsThree.UnsignedIntType
  @inline def FloatType: TextureDataType         = raw.jsThree.FloatType
  @inline def UnsignedShort4444Type: PixelType   = raw.jsThree.UnsignedShort4444Type
  @inline def UnsignedShort5551Type: PixelType   = raw.jsThree.UnsignedShort5551Type
  @inline def UnsignedShort565Type: PixelType    = raw.jsThree.UnsignedShort565Type
  @inline def AlphaFormat: PixelFormat           = raw.jsThree.AlphaFormat
  @inline def RGBFormat: PixelFormat             = raw.jsThree.RGBFormat
  @inline def RGBAFormat: PixelFormat            = raw.jsThree.RGBAFormat
  @inline def LuminanceFormat: PixelFormat       = raw.jsThree.LuminanceFormat
  @inline def LuminanceAlphaFormat: PixelFormat  = raw.jsThree.LuminanceAlphaFormat
  @inline def RGB_S3TC_DXT1_Format: CompressedPixelFormat =
    raw.jsThree.RGBA_S3TC_DXT1_Format
  @inline def RGBA_S3TC_DXT1_Format: CompressedPixelFormat =
    raw.jsThree.RGBA_S3TC_DXT3_Format
  @inline def RGBA_S3TC_DXT3_Format: CompressedPixelFormat =
    raw.jsThree.RGBA_S3TC_DXT5_Format
  @inline def RGBA_S3TC_DXT5_Format: CompressedPixelFormat =
    raw.jsThree.RGBA_S3TC_DXT5_Format
  @inline def Math: Math               = raw.jsThree.Math
  @inline def LineStrip: LineType      = raw.jsThree.LineStrip
  @inline def LinePieces: LineType     = raw.jsThree.LinePieces
  @inline def ShaderChunk: ShaderChunk = raw.jsThree.ShaderChunk

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

  // TODO: Needs sorting into their own files
  type Animation     = raw.Animation
  type AnimationData = raw.AnimationData
  @inline def AnimationHandler = raw.AnimationHandler
  type ArcCurve                 = raw.ArcCurve
  type ArrowHelper              = raw.ArrowHelper
  type AxisHelper               = raw.AxisHelper
  type Bone                     = raw.Bone
  type BoundingBox              = raw.BoundingBox
  type BoundingBox3D            = raw.BoundingBox3D
  type BoundingBoxHelper        = raw.BoundingBoxHelper
  type BoundingSphere           = raw.BoundingSphere
  type Box2                     = raw.Box2
  type BoxGeometry              = raw.BoxGeometry
  type BoxHelper                = raw.BoxHelper
  type BufferAttribute          = raw.BufferAttribute
  type BufferGeometry           = raw.BufferGeometry
  type BufferGeometryLoader     = raw.BufferGeometryLoader
  type Cache                    = raw.Cache
  type CameraHelper             = raw.CameraHelper
  type CanvasRenderer           = raw.CanvasRenderer
  type CanvasRendererParameters = raw.CanvasRendererParameters
  type CircleGeometry           = raw.CircleGeometry
  type Clock                    = raw.Clock
  type ClosedSplineCurve3       = raw.ClosedSplineCurve3
  type Colors                   = raw.Colors
  @inline def Colors = raw.Colors
  type Combine = raw.Combine
  @inline def Combine = raw.Combine
  type CompressedPixelFormat = raw.CompressedPixelFormat
  @inline def CompressedPixelFormat = raw.CompressedPixelFormat
  type CompressedTexture = raw.CompressedTexture
  type CubeCamera        = raw.CubeCamera
  type CubeGeometry      = raw.CubeGeometry
  type CubeTexture       = raw.CubeTexture
  type CubicBezierCurve  = raw.CubicBezierCurve
  type CubicBezierCurve3 = raw.CubicBezierCurve3
  type Curve             = raw.Curve
  @inline def Curve = raw.Curve
  type CurvePath              = raw.CurvePath
  type CylinderGeometry       = raw.CylinderGeometry
  type DataTexture            = raw.DataTexture
  type DepthPassPlugin        = raw.DepthPassPlugin
  type DirectionalLightHelper = raw.DirectionalLightHelper
  type EdgesHelper            = raw.EdgesHelper
  type EllipseCurve           = raw.EllipseCurve
  type ExtrudeGeometry        = raw.ExtrudeGeometry
  type FaceNormalsHelper      = raw.FaceNormalsHelper
  type Float32Attribute       = raw.Float32Attribute
  type Float64Attribute       = raw.Float64Attribute
  @inline def FontUtils = raw.FontUtils
  type Frustum  = raw.Frustum
  type Geometry = raw.Geometry
  @inline def GeometryUtils = raw.GeometryUtils
  type GridHelper            = raw.GridHelper
  type Gyroscope             = raw.Gyroscope
  type HemisphereLightHelper = raw.HemisphereLightHelper
  type IcosahedronGeometry   = raw.IcosahedronGeometry
  type ImageLoader           = raw.ImageLoader
  @inline def ImageUtils = raw.ImageUtils
  type ImmediateRenderObject        = raw.ImmediateRenderObject
  type Int8Attribute                = raw.Int8Attribute
  type Int16Attribute               = raw.Int16Attribute
  type Int32Attribute               = raw.Int32Attribute
  type JSONLoader                   = raw.JSONLoader
  type JSONLoaderResultGeometry     = raw.JSonLoaderResultGeometry
  type KeyFrame                     = raw.KeyFrame
  type KeyFrameAnimation            = raw.KeyFrameAnimation
  type KeyFrames                    = raw.KeyFrames
  type LatheGeometry                = raw.LatheGeometry
  type LensFlare                    = raw.LensFlare
  type LensFlarePlugin              = raw.LensFlarePlugin
  type LensFlareProperty            = raw.LensFlareProperty
  type Line                         = raw.Line
  type LineBasicMaterial            = raw.LineBasicMaterial
  type LineBasicMaterialParameters  = raw.LineBasicMaterialParameters
  type LineCurve                    = raw.LineCurve
  type LineCurve3                   = raw.LineCurve3
  type LineDashedMaterial           = raw.LineDashedMaterial
  type LineDashedMaterialParameters = raw.LineDashedMaterialParameters
  type LineType                     = raw.LineType
  @inline def LineType = raw.LineType
  type Loader = raw.Loader
  @inline def Loader = raw.Loader
  type LoaderHandler                 = raw.LoaderHandler
  type LoadingManager                = raw.LoadingManager
  type LOD                           = raw.LOD
  type MaterialLoader                = raw.MaterialLoader
  type MaterialParameters            = raw.MaterialParameters
  type Math                          = raw.Math
  type Mesh                          = raw.Mesh
  type MeshBasicMaterial             = raw.MeshBasicMaterial
  type MeshBasicMaterialParameters   = raw.MeshBasicMaterialParameters
  type MeshDepthMaterial             = raw.MeshDepthMaterial
  type MeshDepthMaterialParameters   = raw.MeshDepthMaterialParameters
  type MeshFaceMaterial              = raw.MeshFaceMaterial
  type MeshLambertMaterial           = raw.MeshLambertMaterial
  type MeshLambertMaterialParameters = raw.MeshLambertMaterialParameters
  type MeshNormalMaterial            = raw.MeshNormalMaterial
  type MeshNormalMaterialParameters  = raw.MeshNormalMaterialParameters
  type MeshPhongMaterial             = raw.MeshPhongMaterial
  type MeshPhongMaterialParameters   = raw.MeshPhongMaterialParameters
  type MorphAnimation                = raw.MorphAnimation
  type MorphAnimMesh                 = raw.MorphAnimMesh
  type MorphBlendMesh                = raw.MorphBlendMesh
  type MorphBlendMeshAnimation       = raw.MorphBlendMeshAnimation
  type MorphColor                    = raw.MorphColor
  type MorphNormals                  = raw.MorphNormals
  type MorphTarget                   = raw.MorphTarget
  type ObjectLoader                  = raw.ObjectLoader
  type OctahedronGeometry            = raw.OctahedronGeometry
  type OrthographicCamera            = raw.OrthographicCamera
  type ParametricGeometry            = raw.ParametricGeometry
  type ParticleBasicMaterial         = raw.ParticleBasicMaterial
  type ParticleSystemMaterial        = raw.ParticleSystemMaterial
  type Path                          = raw.Path
  type PathAction                    = raw.PathAction
  type PathActions                   = raw.PathActions
  @inline def PathActions = raw.PathActions
  type PerspectiveCamera            = raw.PerspectiveCamera
  type PlaneGeometry                = raw.PlaneGeometry
  type PointCloud                   = raw.PointCloud
  type PointCloudMaterial           = raw.PointCloudMaterial
  type PointCloudMaterialParameters = raw.PointCloudMaterialParameters
  type PointLightHelper             = raw.PointLightHelper
  type PolyhedronGeometry           = raw.PolyhedronGeometry
  type Progress                     = raw.Progress
  type QuadraticBezierCurve         = raw.QuadraticBezierCurve
  type QuadraticBezierCurve3        = raw.QuadraticBezierCurve3
  type RawShaderMaterial            = raw.RawShaderMaterial
  type RenderableFace               = raw.RenderableFace
  type RenderableLine               = raw.RenderableLine
  type RenderableObject             = raw.RenderableObject
  type RenderableSprite             = raw.RenderableSprite
  type RenderableVertex             = raw.RenderableVertex
  @inline def SceneUtils = raw.SceneUtils
  type Shader      = raw.Shader
  type ShaderChunk = raw.ShaderChunk
  @inline def ShaderFlares = raw.ShaderFlares
  @inline def ShaderLib    = raw.ShaderLib
  type ShaderMaterial           = raw.ShaderMaterial
  type ShaderMaterialParameters = raw.ShaderMaterialParameters
  type Shading                  = raw.Shading
  @inline def Shading = raw.Shading
  type Shape                          = raw.Shape
  type ShapeGeometry                  = raw.ShapeGeometry
  type Skeleton                       = raw.Skeleton
  type SkeletonHelper                 = raw.SkeletonHelper
  type SkinnedMesh                    = raw.SkinnedMesh
  type SphereGeometry                 = raw.SphereGeometry
  type Spline                         = raw.Spline
  type SplineControlPoint             = raw.SplineControlPoint
  type SplineCurve                    = raw.SplineCurve
  type SplineCurve3                   = raw.SplineCurve3
  type SpotLightHelper                = raw.SpotLightHelper
  type Sprite                         = raw.Sprite
  type SpriteCanvasMaterial           = raw.SpriteCanvasMaterial
  type SpriteCanvasMaterialParameters = raw.SpriteCanvasMaterialParameters
  type SpritePlugin                   = raw.SpritePlugin
  type Stats                          = raw.Stats
  type TetrahedronGeometry            = raw.TetrahedronGeometry
  type TextGeometry                   = raw.TextGeometry
  type TextGeometryParameters         = raw.TextGeometryParameters
  type TextureLoader                  = raw.TextureLoader
  type TorusGeometry                  = raw.TorusGeometry
  type TorusKnotGeometry              = raw.TorusKnotGeometry
  type Triangle                       = raw.Triangle
  @inline def Triangle = raw.Triangle
  type TubeGeometry          = raw.TubeGeometry
  type TypefaceData          = raw.TypefaceData
  type Uint8Attribute        = raw.Uint8Attribute
  type Uint8ClampedAttribute = raw.Uint8ClampedAttribute
  type Uint16Attribute       = raw.Uint16Attribute
  type Uint32Attribute       = raw.Uint32Attribute
  @inline def UniformsLib   = raw.UniformsLib
  @inline def UniformsUtils = raw.UniformsUtils
  type Vector4               = raw.Vector4
  type VertexNormalsHelper   = raw.VertexNormalsHelper
  type VertexTangentsHelper  = raw.VertexTangentsHelper
  type WebGLProgram          = raw.WebGLProgram
  type WebGLRenderTargetCUbe = raw.WebGLRenderTargetCube
  type WebGLShader           = raw.WebGLShader
  type WireframeHelper       = raw.WireframeHelper
  type XHRLoader             = raw.XHRLoader
}
