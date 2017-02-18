package snowy.client

import org.denigma.threejs._
import org.scalajs.dom.raw.HTMLCanvasElement
import org.scalajs.dom.{document, window}

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSName, ScalaJSDefined}

@js.native
@JSName("THREE.CylinderGeometry")
class CylinderGeometry2 extends Geometry {
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

object CDraw2 {
  def all(): Unit = {
    val scene = new Scene()
    val camera =
      new PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 1000)
    val renderer = new WebGLRenderer {
      js.Dynamic.literal(
        antialias = true,
        alpha = true
      ).asInstanceOf[WebGLRendererParameters]
    }
    renderer.setSize(window.innerWidth, window.innerHeight)
    renderer.setClearColor(new Color(0xfff6e6), 1)
    renderer.shadowMapEnabled = true
    renderer.shadowMapType = THREE.PCFSoftShadowMap
    document.body.appendChild(renderer.domElement)

    camera.position.set(200, 100, 200)
    camera.lookAt(scene.position)

    val trunkGeo  = new BoxGeometry(1.5, 6.25, 1.5)
    val leave1Geo = new CylinderGeometry2(0, 5, 7, 4, 1, false, 0.783, math.Pi * 2)
    val leave2Geo = new CylinderGeometry2(0, 3.5, 5, 4, 1, false, 0.8, math.Pi * 2)

    val trunkMat = new MeshPhongMaterial {
      color = new Color(0x502A2A)
      shading = THREE.FlatShading
    }
    val leave1Mat = new MeshPhongMaterial {
      color = new Color(0x658033)
      shading = THREE.FlatShading
    }
    val leave2Mat = new MeshPhongMaterial {
      color = new Color(0x81A442)
      shading = THREE.FlatShading
    }

    val trunk  = new Mesh(trunkGeo, trunkMat)
    val leave1 = new Mesh(leave1Geo, leave1Mat)
    val leave2 = new Mesh(leave2Geo, leave2Mat)

    val amb   = new AmbientLight(0x888888)
    val light = new PointLight(0xffffff)

    light.position.set(200, 100, 200)
    scene.add(amb)
    scene.add(light)

    var x = Math.random() * 400
    var z = Math.random() * 200

    trunk.position.y = -5
    trunk.position.x = x
    trunk.position.z = z
    trunk.castShadow = true

    leave1.position.y = 2.1
    leave1.position.x = x
    leave1.position.z = z
    leave1.castShadow = true

    leave2.position.y = 4
    leave2.position.x = x
    leave2.position.z = z
    leave2.castShadow = true

    scene.add(trunk)
    scene.add(leave1)
    scene.add(leave2)

    val time = 3233
    camera.position.x = Math.cos(time / 5000) * 250 + 350
    camera.position.z = Math.sin(time / 5000) * 50 + 250
    //light.position.x = camera.position.x
    light.position.z = camera.position.z
    renderer.render(scene, camera)
  }

}
