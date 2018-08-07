package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._
import scala.scalajs.js.|

@js.native
@js.annotation.JSGlobal("Material")
class Material extends EventDispatcher {
  /**
     * Sets the alpha value to be used when running an alpha test. Default is 0.
     */
  var alphaTest: Double = js.native
  /**
     * Blending destination. It's one of the blending mode constants defined in Three.js. Default is { OneMinusSrcAlphaFactor}.
     */
  var blendDst: BlendingDstFactor = js.native
  /**
     * The tranparency of the .blendDst. Default is null.
     */
  var blendDstAlpha: Double = js.native
  /**
     * Blending equation to use when applying blending. It's one of the constants defined in Three.js. Default is { AddEquation}.
     */
  var blendEquation: BlendingEquation = js.native
  /**
     * The tranparency of the .blendEquation. Default is null.
     */
  var blendEquationAlpha: Double = js.native
  /**
     * Which blending to use when displaying objects with this material. Default is { NormalBlending}.
     */
  var blending: Blending = js.native
  /**
     * Blending source. It's one of the blending mode constants defined in Three.js. Default is { SrcAlphaFactor}.
     */
  var blendSrc: BlendingSrcFactor | BlendingDstFactor = js.native
  /**
     * The tranparency of the .blendSrc. Default is null.
     */
  var blendSrcAlpha: Double = js.native
  /**
     * Changes the behavior of clipping planes so that only their intersection is clipped, rather than their union. Default is false.
     */
  var clipIntersection: Boolean = js.native
  /**
     * User-defined clipping planes specified as THREE.Plane objects in world space. These planes apply to the objects this material is attached to. Points in space whose signed distance to the plane is negative are clipped (not rendered). See the WebGL / clipping /intersection example. Default is null.
     */
  var clippingPlanes: js.Any = js.native
  /**
     * Defines whether to clip shadows according to the clipping planes specified on this material. Default is false.
     */
  var clipShadows: Boolean = js.native
  /**
     * Whether to render the material's color. This can be used in conjunction with a mesh's .renderOrder property to create invisible objects that occlude other objects. Default is true.
     */
  var colorWrite: Boolean = js.native
  /**
     * Which depth function to use. Default is { LessEqualDepth}. See the depth mode constants for all possible values.
     */
  var depthFunc: DepthModes = js.native
  /**
     * Whether to have depth test enabled when rendering this material. Default is true.
     */
  var depthTest: Boolean = js.native
  /**
     * Whether rendering this material has any effect on the depth buffer. Default is true.
     * When drawing 2D overlays it can be useful to disable the depth writing in order to layer several things together without creating z-index artifacts.
     */
  var depthWrite: Boolean = js.native
  /**
     * Whether the material is affected by fog. Default is true.
     */
  var fog: Boolean = js.native
  /**
     * Unique number of this material instance.
     */
  var id: Double = js.native
  /**
     * Used to check whether this or derived classes are materials. Default is true.
     * You should not change this, as it used internally for optimisation.
     */
  var isMaterial: Boolean = js.native
  /**
     * Whether the material is affected by lights. Default is true.
     */
  var lights: Boolean = js.native
  /**
     * Material name. Default is an empty string.
     */
  var name: String = js.native
  /**
     * Specifies that the material needs to be updated, WebGL wise. Set it to true if you made changes that need to be reflected in WebGL.
     * This property is automatically set to true when instancing a new material.
     */
  var needsUpdate: Boolean = js.native
  /**
     * Opacity. Default is 1.
     */
  var opacity: Double = js.native
  /**
     * Enables/disables overdraw. If greater than zero, polygons are drawn slightly bigger in order to fix antialiasing gaps when using the CanvasRenderer. Default is 0.
     */
  var overdraw: Double = js.native
  /**
     * Whether to use polygon offset. Default is false. This corresponds to the POLYGON_OFFSET_FILL WebGL feature.
     */
  var polygonOffset: Boolean = js.native
  /**
     * Sets the polygon offset factor. Default is 0.
     */
  var polygonOffsetFactor: Double = js.native
  /**
     * Sets the polygon offset units. Default is 0.
     */
  var polygonOffsetUnits: Double = js.native
  /**
     * Override the renderer's default precision for this material. Can be "highp", "mediump" or "lowp". Defaults is null.
     */
  var precision: String | String | String | Null = js.native
  /**
     * Whether to premultiply the alpha (transparency) value. See WebGL / Materials / Transparency for an example of the difference. Default is false.
     */
  var premultipliedAlpha: Boolean = js.native
  /**
     * Whether to apply dithering to the color to remove the appearance of banding. Default is false.
     */
  var dithering: Boolean = js.native
  /**
     * Define whether the material is rendered with flat shading. Default is false.
     */
  var flatShading: Boolean = js.native
  /**
     * Defines which of the face sides will be rendered - front, back or both.
     * Default is THREE.FrontSide. Other options are THREE.BackSide and THREE.DoubleSide.
     */
  var side: Side = js.native
  /**
     * Defines whether this material is transparent. This has an effect on rendering as transparent objects need special treatment and are rendered after non-transparent objects.
     * When set to true, the extent to which the material is transparent is controlled by setting it's .opacity property.
     * Default is false.
     */
  var transparent: Boolean = js.native
  /**
     * Value is the string 'Material'. This shouldn't be changed, and can be used to find all objects of this type in a scene.
     */
  var `type`: String = js.native
  /**
     * UUID of this material instance. This gets automatically assigned, so this shouldn't be edited.
     */
  var uuid: String = js.native
  /**
     * Defines whether vertex coloring is used. Default is THREE.NoColors. Other options are THREE.VertexColors and THREE.FaceColors.
     */
  var vertexColors: Colors = js.native
  /**
     * Defines whether this material is visible. Default is true.
     */
  var visible: Boolean = js.native
  /**
     * An object that can be used to store custom data about the Material. It should not hold references to functions as these will not be cloned.
     */
  var userData: js.Any = js.native
  /**
     * Return a new material with the same parameters as this material.
     */
  /**
     * Copy the parameters from the passed material into this material.
     * @param material
     */
  def copy(material: Material): js.Dynamic = js.native
  /**
     * This disposes the material. Textures of a material don't get disposed. These needs to be disposed by { Texture}.
     */
  def dispose(): Unit = js.native
  /**
     * Sets the properties based on the values.
     * @param values A container with parameters.
     */
  def setValues(values: MaterialParameters): Unit = js.native
  /**
     * Convert the material to three.js JSON format.
     * @param meta Object containing metadata such as textures or images for the material.
     */
  def toJSON(meta: js.Any = js.native): js.Dynamic = js.native
  /**
     * Call .dispatchEvent ( { type: 'update' }) on the material.
     */
  def update(): Unit = js.native
}