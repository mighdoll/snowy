package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.Light")
class Light(var color: Double = js.native, intensity: Double = js.native)
    extends Object3D {
  def clone(light: Light): Light = js.native
}

@js.native
@JSName("THREE.AmbientLight")
class AmbientLight(color: Double, intensity: Double = js.native)
    extends Light(color, intensity) {
  override def clone(): AmbientLight = js.native
}

@js.native
@JSName("THREE.AreaLight")
class AreaLight(color: Double = js.native, intensity: Double = js.native)
    extends Light(color, intensity) {
  var normal: Vector3              = js.native
  var right: Vector3               = js.native
  var width: Double                = js.native
  var height: Double               = js.native
  var constantAttenuation: Double  = js.native
  var linearAttenuation: Double    = js.native
  var quadraticAttenuation: Double = js.native
}

@js.native
@JSName("THREE.DirectionalLight")
class DirectionalLight(color: Double = js.native, intensity: Double = js.native)
    extends Light(color, intensity) {
  var target: Object3D = js.native

  var onlyShadow: Boolean                            = js.native
  var shadowCameraNear: Double                       = js.native
  var shadowCameraFar: Double                        = js.native
  var shadowCameraLeft: Double                       = js.native
  var shadowCameraRight: Double                      = js.native
  var shadowCameraTop: Double                        = js.native
  var shadowCameraBottom: Double                     = js.native
  var shadowCameraVisible: Boolean                   = js.native
  var shadowBias: Double                             = js.native
  var shadowDarkness: Double                         = js.native
  var shadowMapWidth: Double                         = js.native
  var shadowMapHeight: Double                        = js.native
  var shadowCascade: Boolean                         = js.native
  var shadowCascadeOffset: Vector3                   = js.native
  var shadowCascadeCount: Double                     = js.native
  var shadowCascadeBias: js.Array[Double]            = js.native
  var shadowCascadeWidth: js.Array[Double]           = js.native
  var shadowCascadeHeight: js.Array[Double]          = js.native
  var shadowCascadeNearZ: js.Array[Double]           = js.native
  var shadowCascadeFarZ: js.Array[Double]            = js.native
  var shadowCascadeArray: js.Array[DirectionalLight] = js.native
  var shadowMap: RenderTarget                        = js.native
  var shadowMapSize: Double                          = js.native
  var shadowCamera: Camera                           = js.native
  var shadowMatrix: Matrix4                          = js.native
  override def clone(): DirectionalLight             = js.native
}

@js.native
@JSName("THREE.HemisphereLight")
class HemisphereLight(var skyColor: Double = js.native,
                      var groundColor: Double = js.native,
                      intensity: Double = js.native)
    extends Light(skyColor, intensity) {
  override def clone(): HemisphereLight = js.native
}

@js.native
@JSName("THREE.PointLight")
class PointLight(color: Double = js.native,
                 intensity: Double = js.native,
                 var distance: Double = js.native)
    extends Light(color, intensity) {
  override def clone(): PointLight = js.native
}

@js.native
@JSName("THREE.SpotLight")
class SpotLight(
      color: Double = js.native,
      intensity: Double = js.native,
      var distance: Double = js.native,
      var angle: Double = js.native,
      var exponent: Double = js.native
) extends Light(color, intensity) {
  var target: Object3D = js.native

  var onlyShadow: Boolean          = js.native
  var shadowCameraNear: Double     = js.native
  var shadowCameraFar: Double      = js.native
  var shadowCameraFov: Double      = js.native
  var shadowCameraVisible: Boolean = js.native
  var shadowBias: Double           = js.native
  var shadowDarkness: Double       = js.native
  var shadowMapWidth: Double       = js.native
  var shadowMapHeight: Double      = js.native
  var shadowMap: RenderTarget      = js.native
  var shadowMapSize: Vector2       = js.native
  var shadowCamera: Camera         = js.native
  var shadowMatrix: Matrix4        = js.native
  override def clone(): SpotLight  = js.native
}
