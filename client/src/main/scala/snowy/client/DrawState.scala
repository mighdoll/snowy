package snowy.client

import minithree.THREE
import minithree.THREE.{Object3D, Vector3}
import minithree.raw.{MeshPhongMaterialParameters, Stats}
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event
import org.scalajs.dom.window
import snowy.GameClientProtocol.Scoreboard
import snowy.GameConstants
import snowy.client.ThreeMain._
import snowy.connection.GameState
import snowy.draw.{AddGrid, ThreeSleds, ThreeSnowballs, ThreeTrees}
import snowy.playfield._
import vector.Vec2d

import scala.collection.mutable
import scala.scalajs.js.Dynamic

class UpdateGroup[A](group: Object3D) {
  private val items = group.children.map(item => new PlayId[A](item.name.toInt) -> item)
  val map           = mutable.HashMap(items: _*)
  def add(item: Object3D): Unit = {
    group.add(item)
    map += (new PlayId[A](item.name.toInt) -> item)
  }
  def remove(item: Object3D): Unit = {
    group.remove(item)
    map -= new PlayId[A](item.name.toInt)
  }
}

object DrawState {
  val scene = new THREE.Scene()
  val camera =
    new THREE.PerspectiveCamera(45, Math.min(getWidth / getHeight, 3), 1, 5000)
  camera.position.set(0, 100, 100)
  camera.lookAt(new THREE.Vector3(0, 0, 0))
  val amb   = new THREE.AmbientLight(0x888888)
  val light = new THREE.DirectionalLight(0xffffff)

  val stats = new Stats()
  /** if the object's position is closer to the wrapped side
    * returns the position with */
  def playfieldWrap(pos: Vector3, mySled: Vector3, wrap: Vector3): Vector3 = {

    /** Return coord or wrapped coord depending on which is closer to center */
    def wrapAxis(coord: Double, center: Double, max: Double): Double = {
      ((coord - center + max / 2) % max + max) % max + center - max / 2
    }

    new Vector3(
      wrapAxis(pos.x, mySled.x, wrap.x),
      pos.y,
      wrapAxis(pos.z, mySled.z, wrap.z)
    )
  }

  def setThreePosition(obj: Object3D,
                       playfieldObject: PlayfieldObject,
                       myPos: Vector3): Unit = {
    val newPos = playfieldWrap(
      new Vector3(playfieldObject.pos.x, 0, playfieldObject.pos.y),
      myPos,
      new Vector3(GameConstants.playfield.x, 0, GameConstants.playfield.y)
    )
    obj.position.x = newPos.x
    obj.position.z = newPos.z
  }

  def removeDeaths[A](group: UpdateGroup[A], deaths: Seq[PlayId[A]]): Unit = {
    for {
      death <- deaths
      id = death.id
      sled <- group.map.get(new PlayId[A](id))
    } {
      group.remove(sled)
    }
  }

  def setup(): Unit = {
    stats.showPanel(1)
    document.body.appendChild(stats.dom)

    scene.add(amb)

    light.position.set(10, 20, 0)
    scene.add(light)

    camera.position.y = 800

    Groups.threeGrid = Some(AddGrid.createGrid())
    Groups.threeGrid.foreach { grid =>
      scene.add(grid)
    }

    scene.add(Groups.threeTrees)
    scene.add(Groups.threeSnowballs)
    scene.add(Groups.threeSleds)
  }

  // TODO time to rename drawState? It's no longer in response to State messages..
  def drawState(snowballs: Store[Snowball],
                sleds: Store[Sled],
                mySled: Sled,
                trees: Store[Tree],
                border: Vec2d,
                scoreboard: Scoreboard): Unit = {
    stats.begin()
    val myPos = new Vector3(mySled.pos.x, 0, mySled.pos.y)
    ThreeTrees.updateThreeTrees(trees.items, myPos)
    ThreeSnowballs.updateThreeSnowballs(snowballs.items, myPos)
    ThreeSleds.updateThreeSleds(sleds.items, mySled)

    camera.position.x = mySled._position.x
    camera.position.z = mySled._position.y + 400
    camera.lookAt(new THREE.Vector3(mySled._position.x, 0, mySled._position.y))

    renderState()
    stats.end()
  }

  def renderState(): Unit = {
    if (GameState.mySledId.isDefined) renderer.render(scene, camera)
  }

  object Geos {
    val sled     = new THREE.BoxGeometry(2, 2, 2)
    val turret   = new THREE.BoxGeometry(4, 4, 20)
    val ski      = new THREE.BoxGeometry(0.25, 0.125, 3 - 0.25)
    val skiTip   = new THREE.BoxGeometry(0.25, 0.125, 0.25)
    val snowball = new THREE.BoxGeometry(2, 2, 2)
    val health   = new THREE.BoxGeometry(64, 4, 16)
  }

  object Mats {
    val sled = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x2194ce, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val turret = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x222222, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val ski = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x222222, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val skiTip = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0xEE2222, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )

    val snowball = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x222222, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val healthColor = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x7AF431, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val enemyHealth = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0xF43131, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
  }

  object Groups {
    val threeTrees                  = new THREE.Object3D()
    val threeSnowballs              = new THREE.Object3D()
    val threeSleds                  = new THREE.Object3D()
    var threeGrid: Option[Object3D] = None
  }

  window.addEventListener("resize", { _: Event =>
    camera.aspect = Math.min(getWidth / getHeight, 3)
    camera.updateProjectionMatrix()

    renderState()
  })
}
