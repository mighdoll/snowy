package minithree.THREE

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("THREE")
object THREE2 extends js.Object {
  @js.native
  sealed trait LoopModes extends js.Object
  val LoopOnce: LoopModes     = js.native
  val LoopRepeat: LoopModes   = js.native
  val LoopPingPong: LoopModes = js.native

  @js.native
  sealed trait InterpolationModes extends js.Object
  val InterpolateDiscrete: InterpolationModes = js.native
  val InterpolateLinear: InterpolationModes   = js.native
  val InterpolateSmooth: InterpolationModes   = js.native

  @js.native
  sealed trait EndingModes extends js.Object
  val ZeroCurvatureEnding: EndingModes = js.native
  val ZeroSlopeEnding: EndingModes     = js.native
  val WrapAroundEnding: EndingModes    = js.native

  val REVISION: String = js.native

  @js.native
  sealed trait MOUSE extends js.Object
  @js.native
  object MOUSE extends js.Object {
    val LEFT: MOUSE   = js.native
    val MIDDLE: MOUSE = js.native
    val RIGHT: MOUSE  = js.native
  }

  @js.native
  sealed trait BlendingEquation extends js.Object
  val AddEquation: BlendingEquation             = js.native
  val SubtractEquation: BlendingEquation        = js.native
  val ReverseSubtractEquation: BlendingEquation = js.native
  val MinEquation: BlendingEquation             = js.native
  val MaxEquation: BlendingEquation             = js.native

  @js.native
  sealed trait SourceFactor extends js.Object
  @js.native
  sealed trait DestinationFactor extends js.Object
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

  @js.native
  sealed trait DrawMode extends js.Object
  val TrianglesDrawMode: DrawMode     = js.native
  val TriangleStripDrawMode: DrawMode = js.native
  val TriangleFanDrawMode: DrawMode = js.native

  @js.native
  sealed trait Side extends js.Object
  val FrontSide: Side = js.native
  val BackSide: Side = js.native
  val DoubleSide: Side = js.native

  @js.native
  sealed trait Shading extends js.Object
  val SmoothShading: Shading = js.native
  val FlatShading: Shading = js.native

  @js.native
  sealed trait Colors extends js.Object

}
