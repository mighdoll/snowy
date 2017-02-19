package snowy.client

import minithree.THREE._
import org.scalajs.dom.{document, window}

import scala.scalajs.js
import scala.scalajs.js.Dynamic
import scala.scalajs.js.annotation.JSName

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
    val renderer = new WebGLRenderer(
      Dynamic
        .literal(
          antialias = true
        )
        .asInstanceOf[WebGLRendererParameters]
    )
    renderer.setSize(window.innerWidth, window.innerHeight)
    renderer.setClearColor(new Color(0xfff6e6), 1)
    document.body.appendChild(renderer.domElement)

    camera.position.set(200, 100, 200)
    camera.lookAt(scene.position)

    val trunkGeo  = new BoxGeometry(1.5, 6.25, 1.5)
    val leave1Geo = new CylinderGeometry2(0, 5, 7, 4, 1, false, 0.783, math.Pi * 2)
    val leave2Geo = new CylinderGeometry2(0, 3.5, 5, 4, 1, false, 0.8, math.Pi * 2)

    val trunkMat = new MeshPhongMaterial {
      color = new Color(0x502A2A)
      shading = FlatShading
    }
    val leave1Mat = new MeshPhongMaterial {
      color = new Color(0x658033)
      shading = FlatShading
    }
    val leave2Mat = new MeshPhongMaterial {
      color = new Color(0x81A442)
      shading = FlatShading
    }

    val trunk  = new Mesh(trunkGeo, trunkMat)
    val leave1 = new Mesh(leave1Geo, leave1Mat)
    val leave2 = new Mesh(leave2Geo, leave2Mat)

    val amb   = new AmbientLight(0x888888)
    val light = new DirectionalLight(0xffffff)

    light.position.set(0, 20, 10)

    scene.add(amb)
    scene.add(light)

    var x = 300
    var z = 200

    trunk.position.set(x, 3.125, z)
    leave1.position.set(x, 9.75, z)
    leave2.position.set(x, 11.75, z)

    scene.add(trunk)
    scene.add(leave1)
    scene.add(leave2)

    val time = 3233
    camera.position.x = math.cos(time / 5000) * 250 + 350
    camera.position.z = math.sin(time / 5000) * 50 + 250

    renderer.render(scene, camera)
  }

}
