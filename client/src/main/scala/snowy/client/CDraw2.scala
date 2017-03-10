package snowy.client

import minithree.THREE
import minithree.THREE.WebGLRendererParameters
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{document, window}

import scala.scalajs.js
import scala.scalajs.js.Dynamic
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.CylinderGeometry")
class CylinderGeometry2 extends THREE.Geometry {
  def this(radiusTop: Double = js.native,
           radiusBottom: Double = js.native,
           height: Double = js.native,
           radiusSegments: Double = js.native,
           heightSegments: Double = js.native,
           openEnded: Boolean = js.native,
           thetaStart: Double = js.native,
           thetaLength: Double = js.native) = this()
  var parameters: js.Any     = js.native
  var radiusTop: Double      = js.native
  var radiusBottom: Double   = js.native
  var height: Double         = js.native
  var radialSegments: Double = js.native
  var heightSegments: Double = js.native
  var openEnded: Boolean     = js.native
  var thetaStart: Boolean    = js.native
  var thetaLength: Boolean   = js.native
}

@js.native
@JSName("THREE.ConeGeometry")
class ConeGeometry(radius: Double = js.native,
                   height: Double = js.native,
                   radialSegments: Int = js.native,
                   heightSegments: Int = js.native,
                   openEnded: Boolean = js.native,
                   thetaStart: Double = js.native,
                   thetaLength: Double = js.native)
    extends THREE.Geometry {
  var parameters: js.Any = js.native
}
object CDraw2 {
  var width  = window.innerWidth
  var height = window.innerHeight

  val renderer = new THREE.WebGLRenderer(
    Dynamic
      .literal(antialias = false, canvas = document.getElementById("game-c"))
      .asInstanceOf[WebGLRendererParameters]
  )
  renderer.setClearColor(new THREE.Color(0xe7f1fd), 1)
  renderer.setPixelRatio(
    if (!window.devicePixelRatio.isNaN) window.devicePixelRatio else 1
  )

  val scene  = new THREE.Scene()
  var camera = new THREE.PerspectiveCamera(45, width / height, 1, 5000)
  camera.position.set(0, 100, -100)
  camera.lookAt(new THREE.Vector3(0, 0, 0))

  window.addEventListener("resize", { _: Event =>
    resize()
  })
  resize()

  def resize(): Unit = {
    width = window.innerWidth
    height = window.innerHeight
    camera.aspect = Math.min(width / height, 3)
    camera.updateProjectionMatrix()
    renderer.setSize(width, height)
    renderer.render(scene, camera)
  }
}
