package snowy.client

import minithree.THREE
import minithree.THREE.{MeshPhongMaterialParameters, WebGLRendererParameters}
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
    Dynamic.literal(antialias = false).asInstanceOf[WebGLRendererParameters]
  )
  renderer.setClearColor(new THREE.Color(0xfff6e6), 1)
  document.body.appendChild(renderer.domElement)

  var scene  = new THREE.Scene()
  var camera = new THREE.PerspectiveCamera(45, width / height, 1, 1000)
  camera.position.set(0, 100, -100)
  camera.lookAt(new THREE.Vector3(0, 0, 0))

  def all(): Unit = {
    var amb = new THREE.AmbientLight(0x888888)
    scene.add(amb)

    var light = new THREE.DirectionalLight(0xffffff)
    light.position.set(0, 20, 10)
    scene.add(light)

    val trunkGeo     = new THREE.BoxGeometry(2, 10, 2)
    val leave1Geo    = new ConeGeometry(4, 8, 4, 1, false, 0.783, Math.PI * 2)
    val leave2Geo    = new ConeGeometry(3, 4, 4, 1, false, 0.8, Math.PI * 2)
    val cardBigGeo   = new THREE.BoxGeometry(5, 8, 1)
    val cardSmallGeo = new THREE.BoxGeometry(4, 6, 1)
    val trunkMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x502A2A, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    var leave1Mat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x658033, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    var leave2Mat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x81A442, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    var cardMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0xd8bc9d, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )

    var selector = new THREE.Object3D()
    var tree     = new THREE.Object3D()
    var trunk    = new THREE.Mesh(trunkGeo, trunkMat)
    var leave1   = new THREE.Mesh(leave1Geo, leave1Mat)
    var leave2   = new THREE.Mesh(leave2Geo, leave2Mat)
    trunk.position.y = 5
    leave1.position.y = 14
    leave2.position.y = 16

    tree.add(trunk)
    tree.add(leave1)
    tree.add(leave2)

    var tree2 = new THREE.Object3D()
    tree2.add(trunk.clone())
    tree2.add(leave1.clone())
    tree2.add(leave2.clone())

    tree2.rotateOnAxis(new THREE.Vector3(0, 0, 1), 1.5 * Math.PI)
    tree.rotateOnAxis(new THREE.Vector3(0, 0, 1), 0.5 * Math.PI)

    selector.add(tree)
    selector.add(tree2)

    var cardSmall = new THREE.Mesh(cardSmallGeo, cardMat)
    var cardBig   = new THREE.Mesh(cardBigGeo, cardMat)

    var card1 = new THREE.Mesh(cardSmallGeo, cardMat)
    card1.position.set(-6, 4, 0)

    var card2 = new THREE.Mesh(cardBigGeo, cardMat)
    card2.position.set(0, 5, 0)

    var card3 = new THREE.Mesh(cardSmallGeo, cardMat)
    card3.position.set(6, 4, 0)

    selector.add(card1)
    selector.add(card2)
    selector.add(card3)

    selector.position.y = -50
    selector.rotateOnAxis(new THREE.Vector3(1, 0, 0), 0.3 * Math.PI)
    selector.scale.x = 2
    selector.scale.y = 2
    selector.scale.z = 2
    scene.add(selector)

    var snowyText = new THREE.Object3D()
    var matLetters = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0xa3a3a3, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    var geoParams = Dynamic.literal(
      steps = 1,
      amount = 3,
      bevelEnabled = false
    )

    var s = 3

    var shapeS = new THREE.Shape()
    shapeS.moveTo(s * 0 - s * 1.5, s * 0 - s * 2.5)
    shapeS.lineTo(s * 3 - s * 1.5, s * 0 - s * 2.5)
    shapeS.lineTo(s * 3 - s * 1.5, s * 1 - s * 2.5)
    shapeS.lineTo(s * 1 - s * 1.5, s * 1 - s * 2.5)
    shapeS.lineTo(s * 1 - s * 1.5, s * 2 - s * 2.5)
    shapeS.lineTo(s * 3 - s * 1.5, s * 2 - s * 2.5)
    shapeS.lineTo(s * 3 - s * 1.5, s * 5 - s * 2.5)
    shapeS.lineTo(s * 0 - s * 1.5, s * 5 - s * 2.5)
    shapeS.lineTo(s * 0 - s * 1.5, s * 4 - s * 2.5)
    shapeS.lineTo(s * 2 - s * 1.5, s * 4 - s * 2.5)
    shapeS.lineTo(s * 2 - s * 1.5, s * 3 - s * 2.5)
    shapeS.lineTo(s * 0 - s * 1.5, s * 3 - s * 2.5)
    shapeS.lineTo(s * 0 - s * 1.5, s * 0 - s * 2.5)

    var shapeN = new THREE.Shape()
    shapeN.moveTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeN.lineTo(s * 2 - s * 2, s * 0 - s * 2.5)
    shapeN.lineTo(s * 3 - s * 2, s * 4 - s * 2.5)
    shapeN.lineTo(s * 3 - s * 2, s * 0 - s * 2.5)
    shapeN.lineTo(s * 4 - s * 2, s * 0 - s * 2.5)
    shapeN.lineTo(s * 4 - s * 2, s * 5 - s * 2.5)
    shapeN.lineTo(s * 2 - s * 2, s * 5 - s * 2.5)
    shapeN.lineTo(s * 1 - s * 2, s * 1 - s * 2.5)
    shapeN.lineTo(s * 1 - s * 2, s * 5 - s * 2.5)
    shapeN.lineTo(s * 0 - s * 2, s * 5 - s * 2.5)
    shapeN.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)

    var shapeO = new THREE.Shape()
    shapeO.moveTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeO.lineTo(s * 4 - s * 2, s * 0 - s * 2.5)
    shapeO.lineTo(s * 4 - s * 2, s * 5 - s * 2.5)
    shapeO.lineTo(s * 0 - s * 2, s * 5 - s * 2.5)
    shapeO.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeO.lineTo(s * 1 - s * 2, s * 1 - s * 2.5)
    shapeO.lineTo(s * 1 - s * 2, s * 4 - s * 2.5)
    shapeO.lineTo(s * 3 - s * 2, s * 4 - s * 2.5)
    shapeO.lineTo(s * 3 - s * 2, s * 1 - s * 2.5)
    shapeO.lineTo(s * 1 - s * 2, s * 1 - s * 2.5)
    shapeO.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)

    var shapeW = new THREE.Shape()
    shapeW.moveTo(s * 0 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 1 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 1 - s * 2.5, s * 4 - s * 2.5)
    shapeW.lineTo(s * 2 - s * 2.5, s * 4 - s * 2.5)
    shapeW.lineTo(s * 2 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 3 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 3 - s * 2.5, s * 4 - s * 2.5)
    shapeW.lineTo(s * 4 - s * 2.5, s * 4 - s * 2.5)
    shapeW.lineTo(s * 4 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 5 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 5 - s * 2.5, s * 5 - s * 2.5)
    shapeW.lineTo(s * 0 - s * 2.5, s * 5 - s * 2.5)
    shapeW.lineTo(s * 0 - s * 2.5, s * 0 - s * 2.5)

    var shapeY = new THREE.Shape()
    shapeY.moveTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeY.lineTo(s * 1 - s * 2, s * 0 - s * 2.5)
    shapeY.lineTo(s * 2 - s * 2, s * 2 - s * 2.5)
    shapeY.lineTo(s * 3 - s * 2, s * 0 - s * 2.5)
    shapeY.lineTo(s * 4 - s * 2, s * 0 - s * 2.5)
    shapeY.lineTo(s * 2.5 - s * 2, s * 3 - s * 2.5)
    shapeY.lineTo(s * 2.5 - s * 2, s * 5 - s * 2.5)
    shapeY.lineTo(s * 1.5 - s * 2, s * 5 - s * 2.5)
    shapeY.lineTo(s * 1.5 - s * 2, s * 3 - s * 2.5)
    shapeY.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)

    var geoS = new THREE.ExtrudeGeometry(shapeS, geoParams)
    var geoN = new THREE.ExtrudeGeometry(shapeN, geoParams)
    var geoO = new THREE.ExtrudeGeometry(shapeO, geoParams)
    var geoW = new THREE.ExtrudeGeometry(shapeW, geoParams)
    var geoY = new THREE.ExtrudeGeometry(shapeY, geoParams)

    var meshS = new THREE.Mesh(geoS, matLetters)
    var meshN = new THREE.Mesh(geoN, matLetters)
    var meshO = new THREE.Mesh(geoO, matLetters)
    var meshW = new THREE.Mesh(geoW, matLetters)
    var meshY = new THREE.Mesh(geoY, matLetters)

    meshS.position.x = -s * 0
    meshN.position.x = -s * 4
    meshO.position.x = -s * 9
    meshW.position.x = -s * 14
    meshY.position.x = -s * 19

    meshW.rotateOnAxis(new THREE.Vector3(1, 0, 0), 1 * Math.PI)
    meshW.position.z = 3

    meshY.rotateOnAxis(new THREE.Vector3(1, 0, 0), 1 * Math.PI)
    meshY.position.z = 3

    snowyText.add(meshS)
    snowyText.add(meshN)
    snowyText.add(meshO)
    snowyText.add(meshW)
    snowyText.add(meshY)

    snowyText.position.x = s * 10
    snowyText.position.y = 48
    snowyText.rotateOnAxis(new THREE.Vector3(1, 0, 0), 0.3 * Math.PI)
    scene.add(snowyText)

    //TODO: There's got to be a better way to do this...
    window.addEventListener("resize", { _: Event =>
      resize()
    })
    resize()
  }
  def resize(): Unit = {
    val DPR = if (!window.devicePixelRatio.isNaN) window.devicePixelRatio else 1
    width = window.innerWidth
    height = window.innerHeight
    camera.aspect = Math.min(width / height, 3)
    camera.updateProjectionMatrix()
    renderer.setPixelRatio(DPR)
    renderer.setSize(width, height)
    renderer.render(scene, camera)
  }
}
