package snowy.client

import minithree.THREE
import minithree.THREE.{
  Intersection,
  Mesh,
  MeshLambertMaterial,
  MeshLambertMaterialParameters,
  MeshPhongMaterialParameters,
  Vector3,
  WebGLRenderer
}
import org.scalajs.dom._
import org.scalajs.dom.raw.Event
import snowy.client.ClientMain.{getHeight, getWidth}
import snowy.connection.GameState
import snowy.draw.ThreeSleds
import snowy.playfield._
import vector.Vec2d

import scala.scalajs.js
import scala.scalajs.js.Dynamic
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("THREE.ConeGeometry")
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

class LoginScreen(renderer: WebGLRenderer) {
  private val scene = new THREE.Scene()
  private val camera =
    new THREE.PerspectiveCamera(45, Math.min(getWidth / getHeight, 3), 1, 5000)

  private val amb   = new THREE.AmbientLight(0xFFFFFF, 0.7)
  private val light = new THREE.DirectionalLight(0xFFFFFF, 0.3)

  private var connected: Connection = new Connection()
  private var spawned: Boolean      = false
  private var skiColor: SkiColor    = BasicSkis
  private var sledKind: SledKind    = BasicSled

  private val sled = Sled("", Vec2d(0, 0), sledKind, skiColor)
  private var threeSled =
    ThreeSleds.createSled(sled, true, new Vector3(0, 0, 0)).children(1)

  private val gameDiv    = document.getElementById("game-div").asInstanceOf[html.Div]
  private val gameHud    = document.getElementById("game-hud").asInstanceOf[html.Div]
  private val chosenSled = document.getElementById("chosen").asInstanceOf[html.Div]
  private val loginForm  = document.getElementById("login-form").asInstanceOf[html.Form]
  private val textInput  = document.getElementById("username").asInstanceOf[html.Input]
  private val playButton = document.getElementById("play").asInstanceOf[html.Button]

  sealed trait Direction
  case object Left   extends Direction
  case object Right  extends Direction
  case object Middle extends Direction

  private val raycaster                    = new THREE.Raycaster()
  private val mouse                        = new THREE.Vector2()
  private var hover: Direction             = Middle
  private var hoverColor: Option[SkiColor] = Some(BasicSkis)

  private var rejoinScreen = false

  connected.socket.onOpen { _ =>
    playButton.disabled = false
    playButton.innerHTML = "Join Game"
  }

  private def setup(): Unit = {
    positions()
    addGroups()
    addItems()

    textInput.focus()
  }

  setup()

  def positions(): Unit = {
    camera.position.set(0, 100, 100)
    camera.lookAt(new THREE.Vector3(0, 0, 0))

    light.position.set(0, 2, 1)

    Meshes.trunk.position.y = 5
    Meshes.leave1.position.y = 14
    Meshes.leave2.position.y = 16

    Groups.tree.rotation.z = 0.5 * math.Pi
    Groups.tree2.rotation.z = 1.5 * math.Pi

    Meshes.card.position.y = 5

    Groups.selector.position.y = -70
    Groups.selector.rotation.x = 1.7 * math.Pi
    Groups.selector.scale.x = 2
    Groups.selector.scale.y = 2
    Groups.selector.scale.z = 2

    Groups.colorSelector.position.y = -2

    Meshes.meshS.position.x = Shapes.s * 0
    Meshes.meshN.position.x = Shapes.s * 4
    Meshes.meshO.position.x = Shapes.s * 9
    Meshes.meshW.position.x = Shapes.s * 14
    Meshes.meshY.position.x = Shapes.s * 19

    Meshes.meshS.position.z = Shapes.s / 2
    Meshes.meshN.position.z = Shapes.s / 2
    Meshes.meshO.position.z = Shapes.s / 2
    Meshes.meshW.position.z = Shapes.s / 2
    Meshes.meshY.position.z = Shapes.s / 2

    Meshes.meshS.rotation.y = 1 * math.Pi
    Meshes.meshN.rotation.y = 1 * math.Pi
    Meshes.meshO.rotation.y = 1 * math.Pi
    Meshes.meshW.rotation.x = 1 * math.Pi
    Meshes.meshY.rotation.x = 1 * math.Pi

    Groups.snowyText.position.x = -Shapes.s * 10
    Groups.snowyText.position.y = 48

    Groups.snowyText.rotation.x = 1.7 * math.Pi

    Meshes.input.rotation.x = 1.6 * math.Pi

    Meshes.arrow2.position.y = 2

    Groups.arrow.position.x = 52
    Groups.arrow.rotation.z = 1.5 * math.Pi
    Groups.arrow.scale.set(3, 4, 3)
  }

  def addGroups(): Unit = {
    SkiColors.allSkis.zipWithIndex.foreach {
      case (skiColor, index) =>
        val colMat = new THREE.MeshLambertMaterial(
          Dynamic
            .literal(color = skiColor.color.to0x())
            .asInstanceOf[MeshLambertMaterialParameters]
        )
        val colGeo = new THREE.BoxGeometry(2, 2, 2)
        val mesh   = new THREE.Mesh(colGeo, colMat)
        mesh.position.x = (index - SkiColors.allSkis.size / 2) * 2 + 1
        mesh.name = index.toString
        if (index == 0) {
          mesh.scale.y = 2
          mesh.position.y = -1
        }

        Groups.colorSelector.add(mesh)
    }

    Groups.tree.add(Meshes.trunk)
    Groups.tree.add(Meshes.leave1)
    Groups.tree.add(Meshes.leave2)

    Groups.tree2.add(Meshes.trunk.clone())
    Groups.tree2.add(Meshes.leave1.clone())
    Groups.tree2.add(Meshes.leave2.clone())

    Groups.arrow.add(Meshes.arrow1)
    Groups.arrow.add(Meshes.arrow2)

    Groups.selector.add(Groups.tree)
    Groups.selector.add(Groups.tree2)

    Groups.selector.add(Meshes.card)

    Groups.selector.add(Groups.colorSelector)

    Groups.snowyText.add(Meshes.meshS)
    Groups.snowyText.add(Meshes.meshN)
    Groups.snowyText.add(Meshes.meshO)
    Groups.snowyText.add(Meshes.meshW)
    Groups.snowyText.add(Meshes.meshY)
  }

  def addItems(): Unit = {
    scene.add(amb)
    scene.add(light)
    scene.add(Groups.selector)
    scene.add(Groups.snowyText)
    scene.add(Meshes.input)
    scene.add(Groups.arrow)

    updateSelector()

    renderLoginScreen()
  }

  def loginScreenActive(): Boolean = GameState.mySledId.isEmpty || rejoinScreen

  def renderLoginScreen(): Unit = {
    if (loginScreenActive()) renderer.render(scene, camera)
  }

  def updateColors(): Unit = {
    if (GameState.mySledId.isEmpty || rejoinScreen) {
      hoverColor match {
        case Some(color) =>
          val oldColor =
            Groups.colorSelector.children(SkiColors.allSkis.indexOf(skiColor))
          val newColor = Groups.colorSelector.children(SkiColors.allSkis.indexOf(color))
          oldColor.scale.y = 1
          newColor.scale.y = 2
          oldColor.position.y = 0
          newColor.position.y = -1
          skiColor = color

          updateSelector()
          clearConnection()
        case None =>
      }
    }
  }

  def updateSelector(): Unit = {
    val currentIndex = SledKinds.allSleds.indexOf(sledKind)

    if (GameState.mySledId.isEmpty || rejoinScreen) {
      hover match {
        case Left =>
          sledKind =
            if (currentIndex > 0) SledKinds.allSleds(currentIndex - 1)
            else SledKinds.allSleds.last
          chosenSled.innerHTML = "Sled: " + sledKind.toString.replace("Sled", "")
        case Right =>
          sledKind =
            if (currentIndex < SledKinds.allSleds.length - 1)
              SledKinds.allSleds(currentIndex + 1)
            else SledKinds.allSleds.head
          chosenSled.innerHTML = "Sled: " + sledKind.toString.replace("Sled", "")
        case Middle =>
      }
    }

    if (rejoinScreen && hover != Middle) clearConnection()

    scene.remove(threeSled)

    val sled = Sled("", Vec2d(0, 0), sledKind, skiColor)
    threeSled = ThreeSleds.createSled(sled, true, new Vector3(0, 0, 0)).children(1)
    threeSled.scale.multiplyScalar(0.2)

    threeSled.position.y = -52

    threeSled.lookAt(camera.position)
    threeSled.rotation.x += 0.5 * math.Pi
    threeSled.rotation.y = 0.5 * math.Pi
    scene.add(threeSled)

    renderLoginScreen()
  }

  def hoverTree(intersects: js.Array[Intersection]): Unit = {
    val leaf1 = Groups.tree.children(1).asInstanceOf[Mesh]
    val leaf2 = Groups.tree.children(2).asInstanceOf[Mesh]

    val leaf3 = Groups.tree2.children(1).asInstanceOf[Mesh]
    val leaf4 = Groups.tree2.children(2).asInstanceOf[Mesh]

    leaf1.material.asInstanceOf[MeshLambertMaterial].emissive.setHex(0x0)
    leaf2.material.asInstanceOf[MeshLambertMaterial].emissive.setHex(0x0)
    leaf1.name = "right"
    leaf2.name = "right"
    leaf3.material.asInstanceOf[MeshLambertMaterial].emissive.setHex(0x0)
    leaf4.material.asInstanceOf[MeshLambertMaterial].emissive.setHex(0x0)
    leaf3.name = "left"
    leaf4.name = "left"

    if (intersects.length > 0) {
      val mat = leaf1.material.clone().asInstanceOf[MeshLambertMaterial]
      mat.emissive.setHex(0x222222)
      val mat2 = leaf2.material.clone().asInstanceOf[MeshLambertMaterial]
      mat2.emissive.setHex(0x222222)
      if (intersects(0).`object`.name == "right") {
        leaf1.material = mat
        leaf2.material = mat2
        hover = Left
      } else {
        leaf3.material = mat
        leaf4.material = mat2
        hover = Right
      }
    } else {
      hover = Middle
    }
  }

  def hoverColor(intersects: js.Array[Intersection]): Unit = {
    Groups.colorSelector.children = Groups.colorSelector.children.map { color =>
      val colorMesh = color.asInstanceOf[Mesh]
      colorMesh.material.asInstanceOf[MeshLambertMaterial].emissive.setHex(0x0)
      colorMesh
    }
    if (intersects.length > 0) {
      val hoverObject = intersects(0).`object`.asInstanceOf[Mesh]
      val newMat      = hoverObject.material.clone().asInstanceOf[MeshLambertMaterial]
      newMat.emissive.setHex(0x222222)
      hoverObject.material = newMat
      hoverColor = Some(SkiColors.allSkis(hoverObject.name.toInt))
    } else { hoverColor = None }
  }

  def selectorHover(e: MouseEvent): Unit = {
    mouse.x = (e.clientX / getWidth) * 2 - 1
    mouse.y = (e.clientY / getHeight) * -2 + 1

    raycaster.setFromCamera(mouse, camera)

    val intersectsTree = raycaster.intersectObjects(
      js.Array(
        Groups.tree.children(1),
        Groups.tree.children(2),
        Groups.tree2.children(1),
        Groups.tree2.children(2)
      )
    )
    hoverTree(intersectsTree)

    val intersectsColor = raycaster.intersectObjects(Groups.colorSelector.children)
    hoverColor(intersectsColor)

    renderLoginScreen()
  }

  window.addEventListener("mousemove", selectorHover, false)
  document.addEventListener("mousedown", { _: Event =>
    updateSelector()
    updateColors()
  }, false)

  def loginPressed(e: Event): Unit = {
    e.preventDefault()
    //Connect to the WebSocket server
    if (!spawned) {
      //TODO: If the socket is not open wait until socket is open to join (connected.socket.onOpen)
      connected.join(
        textInput.value,
        sledKind,
        skiColor
      )
      spawned = true
    } else connected.reSpawn()

    swapScreen(true)

    rejoinScreen = false
    gameHud.classList.remove("hide")
    ClientMain.startRedraw()
  }

  loginForm.addEventListener("submit", loginPressed, false)

  def clearConnection(): Unit = { spawned = false }

  def rejoinPanel() {
    swapScreen(false)

    ClientMain.stopRedraw()
    gameHud.classList.add("hide")
    playButton.focus()
    rejoinScreen = true

    renderLoginScreen()
  }

  /** Swap the login screen and game panel */
  def swapScreen(game: Boolean) {
    if (game) {
      gameDiv.classList.remove("back")
      loginForm.classList.add("hide")
    } else {
      gameDiv.classList.add("back")
      loginForm.classList.remove("hide")
    }
  }

  object Geos {
    val trunk  = new THREE.BoxGeometry(2, 10, 2)
    val leave1 = new ConeGeometry(4, 8, 4, 1, false, 0.783, math.Pi * 2)
    val leave2 = new ConeGeometry(3, 4, 4, 1, false, 0.8, math.Pi * 2)
    val card   = new THREE.BoxGeometry(16, 8, 1)

    // LATER use typed version
    val geoParams = Dynamic.literal(steps = 1, amount = Shapes.s, bevelEnabled = false)

    val geoS = new THREE.ExtrudeGeometry(Shapes.shapeS, geoParams)
    val geoN = new THREE.ExtrudeGeometry(Shapes.shapeN, geoParams)
    val geoO = new THREE.ExtrudeGeometry(Shapes.shapeO, geoParams)
    val geoW = new THREE.ExtrudeGeometry(Shapes.shapeW, geoParams)
    val geoY = new THREE.ExtrudeGeometry(Shapes.shapeY, geoParams)

    val input = new THREE.BoxGeometry(45, 10, 2)
  }

  object Mats {
    val trunk = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0x502A2A)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
    val leave1 = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0x658033)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
    val leave2 = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0x81A442)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
    val card = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0xd8bc9d)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val letters = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0xa3a3a3)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
  }

  object Shapes {
    val s = 3

    val shapeS = new THREE.Shape()
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

    val shapeN = new THREE.Shape()
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

    val shapeO = new THREE.Shape()
    shapeO.moveTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeO.lineTo(s * 4 - s * 2, s * 0 - s * 2.5)
    shapeO.lineTo(s * 4 - s * 2, s * 5 - s * 2.5)
    shapeO.lineTo(s * 0 - s * 2, s * 5 - s * 2.5)
    shapeO.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)

    val shapeOHole = new THREE.Shape()
    shapeOHole.moveTo(s * 1 - s * 2, s * 1 - s * 2.5)
    shapeOHole.lineTo(s * 3 - s * 2, s * 1 - s * 2.5)
    shapeOHole.lineTo(s * 3 - s * 2, s * 4 - s * 2.5)
    shapeOHole.lineTo(s * 1 - s * 2, s * 4 - s * 2.5)
    shapeOHole.lineTo(s * 1 - s * 2, s * 1 - s * 2.5)

    shapeO.holes.push(shapeOHole)

    val shapeW = new THREE.Shape()
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

    val shapeY = new THREE.Shape()
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
  }

  object Meshes {
    val trunk  = new THREE.Mesh(Geos.trunk, Mats.trunk)
    val leave1 = new THREE.Mesh(Geos.leave1, Mats.leave1)
    val leave2 = new THREE.Mesh(Geos.leave2, Mats.leave2)

    val arrow1 = new THREE.Mesh(Geos.leave1, Mats.leave1)
    val arrow2 = new THREE.Mesh(Geos.leave2, Mats.leave2)

    val card = new THREE.Mesh(Geos.card, Mats.card)

    val meshS = new THREE.Mesh(Geos.geoS, Mats.letters)
    val meshN = new THREE.Mesh(Geos.geoN, Mats.letters)
    val meshO = new THREE.Mesh(Geos.geoO, Mats.letters)
    val meshW = new THREE.Mesh(Geos.geoW, Mats.letters)
    val meshY = new THREE.Mesh(Geos.geoY, Mats.letters)

    val input = new THREE.Mesh(Geos.input, Mats.letters)
  }

  object Groups {
    val arrow         = new THREE.Object3D()
    val selector      = new THREE.Object3D()
    val tree          = new THREE.Object3D()
    val tree2         = new THREE.Object3D()
    val snowyText     = new THREE.Object3D()
    val colorSelector = new THREE.Object3D()
  }

  window.addEventListener(
    "resize", { _: Event =>
      camera.aspect = Math.min(getWidth / getHeight, 3)
      camera.updateProjectionMatrix()

      if (loginScreenActive()) ClientMain.resize()
      renderLoginScreen()
    },
    false
  )
}
