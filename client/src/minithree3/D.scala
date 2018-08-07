package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._
@js.native
@js.annotation.JSGlobalScope
object D extends js.Object {
  val REVISION: String = js.native
  // https://developer.mozilla.org/en-US/docs/Web/API/MouseEvent.button
  // GL STATE CONSTANTS
  val CullFaceNone: CullFace = js.native
  val CullFaceBack: CullFace = js.native
  val CullFaceFront: CullFace = js.native
  val CullFaceFrontBack: CullFace = js.native
  val FrontFaceDirectionCW: FrontFaceDirection = js.native
  val FrontFaceDirectionCCW: FrontFaceDirection = js.native
  // Shadowing Type
  val BasicShadowMap: ShadowMapType = js.native
  val PCFShadowMap: ShadowMapType = js.native
  val PCFSoftShadowMap: ShadowMapType = js.native
  // MATERIAL CONSTANTS
  // side
  val FrontSide: Side = js.native
  val BackSide: Side = js.native
  val DoubleSide: Side = js.native
  // shading
  val FlatShading: Shading = js.native
  val SmoothShading: Shading = js.native
  // colors
  val NoColors: Colors = js.native
  val FaceColors: Colors = js.native
  val VertexColors: Colors = js.native
  // blending modes
  val NoBlending: Blending = js.native
  val NormalBlending: Blending = js.native
  val AdditiveBlending: Blending = js.native
  val SubtractiveBlending: Blending = js.native
  val MultiplyBlending: Blending = js.native
  val CustomBlending: Blending = js.native
  // custom blending equations
  // (numbers start from 100 not to clash with other
  //  mappings to OpenGL constants defined in Texture.js)
  val AddEquation: BlendingEquation = js.native
  val SubtractEquation: BlendingEquation = js.native
  val ReverseSubtractEquation: BlendingEquation = js.native
  val MinEquation: BlendingEquation = js.native
  val MaxEquation: BlendingEquation = js.native
  // custom blending destination factors
  val ZeroFactor: BlendingDstFactor = js.native
  val OneFactor: BlendingDstFactor = js.native
  val SrcColorFactor: BlendingDstFactor = js.native
  val OneMinusSrcColorFactor: BlendingDstFactor = js.native
  val SrcAlphaFactor: BlendingDstFactor = js.native
  val OneMinusSrcAlphaFactor: BlendingDstFactor = js.native
  val DstAlphaFactor: BlendingDstFactor = js.native
  val OneMinusDstAlphaFactor: BlendingDstFactor = js.native
  val DstColorFactor: BlendingDstFactor = js.native
  val OneMinusDstColorFactor: BlendingDstFactor = js.native
  // custom blending src factors
  val SrcAlphaSaturateFactor: BlendingSrcFactor = js.native
  // depth modes
  val NeverDepth: DepthModes = js.native
  val AlwaysDepth: DepthModes = js.native
  val LessDepth: DepthModes = js.native
  val LessEqualDepth: DepthModes = js.native
  val EqualDepth: DepthModes = js.native
  val GreaterEqualDepth: DepthModes = js.native
  val GreaterDepth: DepthModes = js.native
  val NotEqualDepth: DepthModes = js.native
  // TEXTURE CONSTANTS
  // Operations
  val MultiplyOperation: Combine = js.native
  val MixOperation: Combine = js.native
  val AddOperation: Combine = js.native
  // Tone Mapping modes
  val NoToneMapping: ToneMapping = js.native
  val LinearToneMapping: ToneMapping = js.native
  val ReinhardToneMapping: ToneMapping = js.native
  val Uncharted2ToneMapping: ToneMapping = js.native
  val CineonToneMapping: ToneMapping = js.native
  // Mapping modes
  val UVMapping: Mapping = js.native
  val CubeReflectionMapping: Mapping = js.native
  val CubeRefractionMapping: Mapping = js.native
  val EquirectangularReflectionMapping: Mapping = js.native
  val EquirectangularRefractionMapping: Mapping = js.native
  val SphericalReflectionMapping: Mapping = js.native
  val CubeUVReflectionMapping: Mapping = js.native
  val CubeUVRefractionMapping: Mapping = js.native
  // Wrapping modes
  val RepeatWrapping: Wrapping = js.native
  val ClampToEdgeWrapping: Wrapping = js.native
  val MirroredRepeatWrapping: Wrapping = js.native
  // Filters
  val NearestFilter: TextureFilter = js.native
  val NearestMipMapNearestFilter: TextureFilter = js.native
  val NearestMipMapLinearFilter: TextureFilter = js.native
  val LinearFilter: TextureFilter = js.native
  val LinearMipMapNearestFilter: TextureFilter = js.native
  val LinearMipMapLinearFilter: TextureFilter = js.native
  // Data types
  val UnsignedByteType: TextureDataType = js.native
  val ByteType: TextureDataType = js.native
  val ShortType: TextureDataType = js.native
  val UnsignedShortType: TextureDataType = js.native
  val IntType: TextureDataType = js.native
  val UnsignedIntType: TextureDataType = js.native
  val FloatType: TextureDataType = js.native
  val HalfFloatType: TextureDataType = js.native
  // Pixel types
  val UnsignedShort4444Type: PixelType = js.native
  val UnsignedShort5551Type: PixelType = js.native
  val UnsignedShort565Type: PixelType = js.native
  val UnsignedInt248Type: PixelType = js.native
  // Pixel formats
  val AlphaFormat: PixelFormat = js.native
  val RGBFormat: PixelFormat = js.native
  val RGBAFormat: PixelFormat = js.native
  val LuminanceFormat: PixelFormat = js.native
  val LuminanceAlphaFormat: PixelFormat = js.native
  val RGBEFormat: PixelFormat = js.native
  val DepthFormat: PixelFormat = js.native
  val DepthStencilFormat: PixelFormat = js.native
  // Compressed texture formats
  // DDS / ST3C Compressed texture formats
  val RGB_S3TC_DXT1_Format: CompressedPixelFormat = js.native
  val RGBA_S3TC_DXT1_Format: CompressedPixelFormat = js.native
  val RGBA_S3TC_DXT3_Format: CompressedPixelFormat = js.native
  val RGBA_S3TC_DXT5_Format: CompressedPixelFormat = js.native
  // PVRTC compressed texture formats
  val RGB_PVRTC_4BPPV1_Format: CompressedPixelFormat = js.native
  val RGB_PVRTC_2BPPV1_Format: CompressedPixelFormat = js.native
  val RGBA_PVRTC_4BPPV1_Format: CompressedPixelFormat = js.native
  val RGBA_PVRTC_2BPPV1_Format: CompressedPixelFormat = js.native
  // ETC compressed texture formats
  val RGB_ETC1_Format: CompressedPixelFormat = js.native
  // Loop styles for AnimationAction
  val LoopOnce: AnimationActionLoopStyles = js.native
  val LoopRepeat: AnimationActionLoopStyles = js.native
  val LoopPingPong: AnimationActionLoopStyles = js.native
  // Interpolation
  val InterpolateDiscrete: InterpolationModes = js.native
  val InterpolateLinear: InterpolationModes = js.native
  val InterpolateSmooth: InterpolationModes = js.native
  // Interpolant ending modes
  val ZeroCurvatureEnding: InterpolationEndingModes = js.native
  val ZeroSlopeEnding: InterpolationEndingModes = js.native
  val WrapAroundEnding: InterpolationEndingModes = js.native
  // Triangle Draw modes
  val TrianglesDrawMode: TrianglesDrawModes = js.native
  val TriangleStripDrawMode: TrianglesDrawModes = js.native
  val TriangleFanDrawMode: TrianglesDrawModes = js.native
  // Texture Encodings
  val LinearEncoding: TextureEncoding = js.native
  val sRGBEncoding: TextureEncoding = js.native
  val GammaEncoding: TextureEncoding = js.native
  val RGBEEncoding: TextureEncoding = js.native
  val LogLuvEncoding: TextureEncoding = js.native
  val RGBM7Encoding: TextureEncoding = js.native
  val RGBM16Encoding: TextureEncoding = js.native
  val RGBDEncoding: TextureEncoding = js.native
  // Depth packing strategies
  val BasicDepthPacking: DepthPackingStrategies = js.native
  val RGBADepthPacking: DepthPackingStrategies = js.native
  // log handlers
  def warn(message: js.Any, optionalParams: js.Any*): Unit = js.native
  def error(message: js.Any, optionalParams: js.Any*): Unit = js.native
  def log(message: js.Any, optionalParams: js.Any*): Unit = js.native
  // Animation ////////////////////////////////////////////////////////////////////////////////////////
  // Cameras ////////////////////////////////////////////////////////////////////////////////////////
  /**
 * Abstract base class for cameras. This class should always be inherited when you build a new camera.
 */
  /**
 * Camera with orthographic projection
 *
 * @example
 * var camera = new THREE.OrthographicCamera( width / - 2, width / 2, height / 2, height / - 2, 1, 1000 );
 * scene.add( camera );
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/cameras/OrthographicCamera.js">src/cameras/OrthographicCamera.js</a>
 */
  /**
 * Camera with perspective projection.
 *
 * # example
 *     var camera = new THREE.PerspectiveCamera( 45, width / height, 1, 1000 );
 *     scene.add( camera );
 *
 * @source https://github.com/mrdoob/three.js/blob/master/src/cameras/PerspectiveCamera.js
 */
  // Core ///////////////////////////////////////////////////////////////////////////////////////////////
  /**
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/BufferAttribute.js">src/core/BufferAttribute.js</a>
 */
  /**
 * @deprecated THREE.Int8Attribute has been removed. Use new THREE.Int8BufferAttribute() instead.
 */
  /**
 * @deprecated THREE.Uint8Attribute has been removed. Use new THREE.Uint8BufferAttribute() instead.
 */
  /**
 * @deprecated THREE.Uint8ClampedAttribute has been removed. Use new THREE.Uint8ClampedBufferAttribute() instead.
 */
  /**
 * @deprecated THREE.Int16Attribute has been removed. Use new THREE.Int16BufferAttribute() instead.
 */
  /**
 * @deprecated THREE.Uint16Attribute has been removed. Use new THREE.Uint16BufferAttribute() instead.
 */
  /**
 * @deprecated THREE.Int32Attribute has been removed. Use new THREE.Int32BufferAttribute() instead.
 */
  /**
 * @deprecated THREE.Uint32Attribute has been removed. Use new THREE.Uint32BufferAttribute() instead.
 */
  /**
 * @deprecated THREE.Float32Attribute has been removed. Use new THREE.Float32BufferAttribute() instead.
 */
  /**
 * @deprecated THREE.Float64Attribute has been removed. Use new THREE.Float64BufferAttribute() instead.
 */
  /**
 * @deprecated Use { BufferAttribute#setDynamic THREE.BufferAttribute().setDynamic( true )} instead.
 */
  /**
 * This is a superefficent class for geometries because it saves all data in buffers.
 * It reduces memory costs and cpu cycles. But it is not as easy to work with because of all the nessecary buffer calculations.
 * It is mainly interesting when working with static objects.
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/BufferGeometry.js">src/core/BufferGeometry.js</a>
 */
  /**
 * Object for keeping track of time.
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/Clock.js">src/core/Clock.js</a>
 */
  /**
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/DirectGeometry.js">src/core/DirectGeometry.js</a>
 */
  /**
 * JavaScript events for custom objects
 *
 * # Example
 *     var Car = function () {
 *
 *         EventDispatcher.call( this );
 *         this.start = function () {
 *
 *             this.dispatchEvent( { type: 'start', message: 'vroom vroom!' } );
 *
 *         };
 *
 *     };
 *
 *     var car = new Car();
 *     car.addEventListener( 'start', function ( event ) {
 *
 *         alert( event.message );
 *
 *     } );
 *     car.start();
 *
 * @source src/core/EventDispatcher.js
 */
  /**
 * Triangle face.
 *
 * # Example
 *     var normal = new THREE.Vector3( 0, 1, 0 );
 *     var color = new THREE.Color( 0xffaa00 );
 *     var face = new THREE.Face3( 0, 1, 2, normal, color, 0 );
 *
 * @source https://github.com/mrdoob/three.js/blob/master/src/core/Face3.js
 */
  /**
 * @deprecated Use { Face3} instead.
 */
  val GeometryIdCount: Double = js.native
  /**
 * Base class for geometries
 *
 * # Example
 *     var geometry = new THREE.Geometry();
 *     geometry.vertices.push( new THREE.Vector3( -10, 10, 0 ) );
 *     geometry.vertices.push( new THREE.Vector3( -10, -10, 0 ) );
 *     geometry.vertices.push( new THREE.Vector3( 10, -10, 0 ) );
 *     geometry.faces.push( new THREE.Face3( 0, 1, 2 ) );
 *     geometry.computeBoundingSphere();
 *
 * @see https://github.com/mrdoob/three.js/blob/master/src/core/Geometry.js
 */
  /**
 * @deprecated
 */
  /**
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/InstancedBufferAttribute.js">src/core/InstancedBufferAttribute.js</a>
 */
  /**
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/InstancedBufferGeometry.js">src/core/InstancedBufferGeometry.js</a>
 */
  /**
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/InterleavedBuffer.js">src/core/InterleavedBuffer.js</a>
 */
  /**
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/InstancedInterleavedBuffer.js">src/core/InstancedInterleavedBuffer.js</a>
 */
  /**
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/core/InterleavedBufferAttribute.js">src/core/InterleavedBufferAttribute.js</a>
 */
  val Object3DIdCount: Double = js.native
  /**
 * Base class for scene graph objects
 */
  // Lights //////////////////////////////////////////////////////////////////////////////////
  /**
 * Abstract base class for lights.
 */
  /**
 * This light's color gets applied to all the objects in the scene globally.
 *
 * # example
 *     var light = new THREE.AmbientLight( 0x404040 ); // soft white light
 *     scene.add( light );
 *
 * @source https://github.com/mrdoob/three.js/blob/master/src/lights/AmbientLight.js
 */
  /**
 * Affects objects using MeshLambertMaterial or MeshPhongMaterial.
 *
 * @example
 * // White directional light at half intensity shining from the top.
 * var directionalLight = new THREE.DirectionalLight( 0xffffff, 0.5 );
 * directionalLight.position.set( 0, 1, 0 );
 * scene.add( directionalLight );
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/lights/DirectionalLight.js">src/lights/DirectionalLight.js</a>
 */
  /**
 * Affects objects using { MeshLambertMaterial} or { MeshPhongMaterial}.
 *
 * @example
 * var light = new THREE.PointLight( 0xff0000, 1, 100 );
 * light.position.set( 50, 50, 50 );
 * scene.add( light );
 */
  /**
 * A point light that can cast shadow in one direction.
 */
  // Loaders //////////////////////////////////////////////////////////////////////////////////
  /**
 * Base class for implementing loaders.
 *
 * Events:
 *     load
 *         Dispatched when the image has completed loading
 *         content — loaded image
 *
 *     error
 *
 *          Dispatched when the image can't be loaded
 *          message — error message
 */
  /**
* Interface for all loaders
* CompressedTextureLoader don't extends Loader class, but have load method
*/
  /**
 * A loader for loading an image.
 * Unlike other loaders, this one emits events instead of using predefined callbacks. So if you're interested in getting notified when things happen, you need to add listeners to the object.
 */
  /**
 * A loader for loading objects in JSON format.
 */
  /**
 * Handles and keeps track of loaded and pending data.
 */
  val DefaultLoadingManager: LoadingManager = js.native
  /**
 * Class for loading a texture.
 * Unlike other loaders, this one emits events instead of using predefined callbacks. So if you're interested in getting notified when things happen, you need to add listeners to the object.
 */
  /**
 * @deprecated since 0.84.0. Use { DataTextureLoader} (renamed)
 */
  // Materials //////////////////////////////////////////////////////////////////////////////////
  val MaterialIdCount: Double = js.native
  /**
 * Materials describe the appearance of objects. They are defined in a (mostly) renderer-independent way, so you don't have to rewrite materials if you decide to use a different renderer.
 */
  /**
 * parameters is an object with one or more properties defining the material's appearance.
 */
  // MultiMaterial does not inherit the Material class in the original code. However, it should treat as Material class.
  // See tests/canvas/canvas_materials.ts.
  /**
 * @deprecated Use an Array instead.
 */
  /**
 * @deprecated Use { MultiMaterial} instead.
 */
  /**
 * @deprecated Use { PointsMaterial THREE.PointsMaterial} instead
 */
  /**
 * @deprecated Use { PointsMaterial THREE.PointsMaterial} instead
 */
  /**
 * @deprecated Use { PointsMaterial THREE.PointsMaterial} instead
 */
  // Math //////////////////////////////////////////////////////////////////////////////////
  /**
 * Represents a color. See also { ColorUtils}.
 *
 * @example
 * var color = new THREE.Color( 0xff0000 );
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/math/Color.js">src/math/Color.js</a>
 */
  /**
 * Frustums are used to determine what is inside the camera's field of view. They help speed up the rendering process.
 */
  /**
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/math/Math.js">src/math/Math.js</a>
 */
  /**
 * ( interface Matrix&lt;T&gt; )
 */
  /**
 * ( class Matrix3 implements Matrix&lt;Matrix3&gt; )
 */
  /**
 * A 4x4 Matrix.
 *
 * @example
 * // Simple rig for rotating around 3 axes
 * var m = new THREE.Matrix4();
 * var m1 = new THREE.Matrix4();
 * var m2 = new THREE.Matrix4();
 * var m3 = new THREE.Matrix4();
 * var alpha = 0;
 * var beta = Math.PI;
 * var gamma = Math.PI/2;
 * m1.makeRotationX( alpha );
 * m2.makeRotationY( beta );
 * m3.makeRotationZ( gamma );
 * m.multiplyMatrices( m1, m2 );
 * m.multiply( m3 );
 */
  /**
 * Implementation of a quaternion. This is used for rotating things without incurring in the dreaded gimbal lock issue, amongst other advantages.
 *
 * @example
 * var quaternion = new THREE.Quaternion();
 * quaternion.setFromAxisAngle( new THREE.Vector3( 0, 1, 0 ), Math.PI / 2 );
 * var vector = new THREE.Vector3( 1, 0, 0 );
 * vector.applyQuaternion( quaternion );
 */
  /**
 * ( interface Vector&lt;T&gt; )
 *
 * Abstract interface of Vector2, Vector3 and Vector4.
 * Currently the members of Vector is NOT type safe because it accepts different typed vectors.
 * Those definitions will be changed when TypeScript innovates Generics to be type safe.
 *
 * @example
 * var v:THREE.Vector = new THREE.Vector3();
 * v.addVectors(new THREE.Vector2(0, 1), new THREE.Vector2(2, 3));    // invalid but compiled successfully
 */
  /**
 * 2D vector.
 *
 * ( class Vector2 implements Vector<Vector2> )
 */
  /**
 * 3D vector.
 *
 * @example
 * var a = new THREE.Vector3( 1, 0, 0 );
 * var b = new THREE.Vector3( 0, 1, 0 );
 * var c = new THREE.Vector3();
 * c.crossVectors( a, b );
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/math/Vector3.js">src/math/Vector3.js</a>
 *
 * ( class Vector3 implements Vector<Vector3> )
 */
  /**
 * @deprecated use { Vector3 THREE.Vector3} instead.
 */
  /**
 * 4D vector.
 *
 * ( class Vector4 implements Vector<Vector4> )
 */
  // Objects //////////////////////////////////////////////////////////////////////////////////
  /**
 * @deprecated
 */
  val LineStrip: Double = js.native
  /**
 * @deprecated
 */
  val LinePieces: Double = js.native
  /**
 * A class for displaying particles in the form of variable size points. For example, if using the WebGLRenderer, the particles are displayed using GL_POINTS.
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/objects/ParticleSystem.js">src/objects/ParticleSystem.js</a>
 */
  /**
 * @deprecated Use { Points THREE.Points} instead.
 */
  /**
 * @deprecated Use { Points THREE.Points} instead.
 */
  /**
 * @deprecated Use { Sprite Sprite} instead.
 */
  // Renderers //////////////////////////////////////////////////////////////////////////////////
  /**
 * The WebGL renderer displays your beautifully crafted scenes using WebGL, if your device supports it.
 * This renderer has way better performance than CanvasRenderer.
 *
 * @see <a href="https://github.com/mrdoob/three.js/blob/master/src/renderers/WebGLRenderer.js">src/renderers/WebGLRenderer.js</a>
 */
  // not defined in the code, used in LightShadow and WebGRenderer classes
  // Renderers / Shaders /////////////////////////////////////////////////////////////////////
  // export let UniformsLib: {
  //     common: {
  //         diffuse: IUniform;
  //         opacity: IUniform;
  //         map: IUniform;
  //         offsetRepeat: IUniform;
  //         specularMap: IUniform;
  //         alphaMap: IUniform;
  //         envMap: IUniform;
  //         flipEnvMap: IUniform;
  //         reflectivity: IUniform;
  //         refractionRation: IUniform;
  //     };
  //     aomap: {
  //         aoMap: IUniform;
  //         aoMapIntensity: IUniform;
  //     };
  //     lightmap: {
  //         lightMap: IUniform;
  //         lightMapIntensity: IUniform;
  //     };
  //     emissivemap: { emissiveMap: IUniform };
  //     bumpmap: {
  //         bumpMap: IUniform;
  //         bumpScale: IUniform;
  //     };
  //     normalmap: {
  //         normalMap: IUniform;
  //         normalScale: IUniform;
  //     };
  //     displacementmap: {
  //         displacementMap: IUniform;
  //         displacementScale: IUniform;
  //         displacementBias: IUniform;
  //     };
  //     roughnessmap: { roughnessMap: IUniform };
  //     metalnessmap: { metalnessMap: IUniform };
  //     fog: {
  //         fogDensity: IUniform;
  //         fogNear: IUniform;
  //         fogFar: IUniform;
  //         fogColor: IUniform;
  //     };
  //     lights: {
  //         ambientLightColor: IUniform
  //         directionalLights: {
  //             value: any[];
  //             properties: {
  //                 direction: {};
  //                 color: {};
  //                 shadow: {};
  //                 shadowBias: {};
  //                 shadowRadius: {};
  //                 shadowMapSize: {};
  //             };
  //         };
  //         directionalShadowMap: IUniform;
  //         directionalShadowMatrix: IUniform;
  //         spotLights: {
  //             value: any[];
  //             properties: {
  //                 color: {};
  //                 position: {};
  //                 direction: {};
  //                 distance: {};
  //                 coneCos: {};
  //                 penumbraCos: {};
  //                 decay: {};
  //                 shadow: {};
  //                 shadowBias: {};
  //                 shadowRadius: {};
  //                 shadowMapSize: {};
  //             };
  //         };
  //         spotShadowMap: IUniform;
  //         spotShadowMatrix: IUniform;
  //         pointLights: {
  //             value: any[];
  //             properties: {
  //                 color: {};
  //                 position: {};
  //                 decay: {};
  //                 distance: {};
  //                 shadow: {};
  //                 shadowBias: {};
  //                 shadowRadius: {};
  //                 shadowMapSize: {};
  //             };
  //         };
  //         pointShadowMap: IUniform;
  //         pointShadowMatrix: IUniform;
  //         hemisphereLigtts: {
  //             value: any[];
  //             properties: {
  //                 direction: {};
  //                 skycolor: {};
  //                 groundColor: {};
  //             };
  //         };
  //     };
  //     points: {
  //         diffuse: IUniform;
  //         opacity: IUniform;
  //         size: IUniform;
  //         scale: IUniform;
  //         map: IUniform;
  //         offsetRepeat: IUniform;
  //     };
  // };
  // Renderers / WebGL /////////////////////////////////////////////////////////////////////
  // Renderers / WebGL / Plugins /////////////////////////////////////////////////////////////////////
  // Scenes /////////////////////////////////////////////////////////////////////
  /**
 * Scenes allow you to set up what and where is to be rendered by three.js. This is where you place objects, lights and cameras.
 */
  /**
 * This class contains the parameters that define linear fog, i.e., that grows linearly denser with the distance.
 */
  /**
 * This class contains the parameters that define linear fog, i.e., that grows exponentially denser with the distance.
 */
  // Textures /////////////////////////////////////////////////////////////////////
  val TextureIdCount: Double = js.native
  // images?: any[], // HTMLImageElement or HTMLCanvasElement
  // mapping?: Mapping,
  // wrapS?: Wrapping,
  // wrapT?: Wrapping,
  // magFilter?: TextureFilter,
  // minFilter?: TextureFilter,
  // format?: PixelFormat,
  // type?: TextureDataType,
  // anisotropy?: number,
  // encoding?: TextureEncoding
  // Extras /////////////////////////////////////////////////////////////////////
  /**
 * @deprecated Use { TextureLoader} instead.
 */
  // Extras / Audio /////////////////////////////////////////////////////////////////////
  val AudioContext: AudioContext = js.native
}