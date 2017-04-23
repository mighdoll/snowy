package snowy.client

import minithree.THREE
import minithree.THREE.{
  MeshBasicMaterialParameters,
  MeshLambertMaterialParameters,
  Object3D,
  Stats,
  Vector3,
  WebGLRenderer
}
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{document, window}
import snowy.GameConstants
import snowy.client.ClientMain.{getHeight, getWidth}
import snowy.connection.GameState
import snowy.connection.GameState.{nextState, nextTimeSlice}
import snowy.draw.{CreateGrid, ThreeSleds, ThreeSnowballs, ThreeTrees}
import snowy.playfield._
import vector.Vec2d

import scala.collection.mutable
import scala.scalajs.js.Dynamic

class UpdateGroup[A](group: Object3D) {
  val map = {
    val items = group.children.map(item => new PlayId[A](item.name.toInt) -> item)
    mutable.HashMap(items: _*)
  }

  def add(item: Object3D): Unit = {
    group.add(item)
    map += (new PlayId[A](item.name.toInt) -> item)
  }
  def remove(item: Object3D): Unit = {
    group.remove(item)
    map -= new PlayId[A](item.name.toInt)
  }
}

object DrawPlayfield {

  def setThreePosition(obj: Object3D,
                       playfieldItem: PlayfieldItem[_],
                       myPos: Vector3): Unit = {
    val newPos = playfieldWrap(
      new Vector3(playfieldItem.position.x, 0, playfieldItem.position.y),
      myPos,
      new Vector3(GameConstants.playfield.x, 0, GameConstants.playfield.y)
    )
    obj.position.x = newPos.x
    obj.position.z = newPos.z
  }

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

  def removeDeaths[A](group: UpdateGroup[A], deaths: Seq[PlayId[A]]): Unit = {
    for {
      death <- deaths
      id = death.id
      sled <- group.map.get(new PlayId[A](id))
    } {
      group.remove(sled)
    }
  }

  object Geos {
    val sled     = new THREE.BoxGeometry(2, 2, 2)
    val turret   = new THREE.BoxGeometry(8, 8, 20)
    val ski      = new THREE.BoxGeometry(0.25, 0.125, 3 - 0.25)
    val skiTip   = new THREE.BoxGeometry(0.25, 0.125, 0.25)
    val snowball = new THREE.BoxGeometry(2, 2, 2)
    val health   = new THREE.PlaneGeometry(64, 16)
  }

  object Mats {
    val sled = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0x2194ce)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
    val turret = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0x222222)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
    val ski = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0x222222)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
    val skiTip = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0xEE2222)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
    val snowball = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0x1878f0)
        .asInstanceOf[MeshLambertMaterialParameters]
    )
    val healthColor = new THREE.MeshBasicMaterial(
      Dynamic
        .literal(
          color = 0x59B224,
          transparent = true,
          depthTest = false
        )
        .asInstanceOf[MeshBasicMaterialParameters]
    )
    val enemyHealth = new THREE.MeshBasicMaterial(
      Dynamic
        .literal(
          color = 0xF43131,
          transparent = true,
          depthTest = false
        )
        .asInstanceOf[MeshBasicMaterialParameters]
    )
  }

  object Groups {
    val threeTrees     = new THREE.Object3D()
    val threeSnowballs = new THREE.Object3D()
    val threeSleds     = new THREE.Object3D()
  }

}

class DrawPlayfield(renderer: WebGLRenderer, threeSleds: ThreeSleds) {
  import DrawPlayfield._
  val scene = new THREE.Scene()
  val camera =
    new THREE.PerspectiveCamera(45, math.min(getWidth / getHeight, 3), 1, 5000)

  val amb   = new THREE.AmbientLight(0xFFFFFF, 0.5)
  val light = new THREE.DirectionalLight(0xFFFFFF, 0.5)

  val stats = new Stats()

  def setup(): Unit = {
    stats.showPanel(0)
    document.body.appendChild(stats.dom)

    camera.position.set(0, 1200, 400)
    camera.lookAt(new THREE.Vector3(0, 0, 0))

    scene.add(amb)

    light.position.set(0, 2, 1)
    scene.add(light)

    scene.add(CreateGrid.newGrid())

    scene.add(Groups.threeTrees)
    scene.add(Groups.threeSnowballs)
    scene.add(Groups.threeSleds)
  }

  setup()

  /** Update the positions of all the three playfield items, then draw to screen */
  def drawPlayfield(snowballs: Set[Snowball],
                    sleds: Set[Sled],
                    mySled: Sled,
                    trees: Set[Tree],
                    border: Vec2d): Unit = {
    stats.begin()
    val myPos = new Vector3(mySled.position.x, 0, mySled.position.y)
    ThreeTrees.updateThreeTrees(trees, myPos)
    ThreeSnowballs.updateThreeSnowballs(snowballs, myPos)
    threeSleds.updateThreeSleds(sleds, mySled)

    camera.position.set(mySled.position.x, 1200 + myPos.y, mySled.position.y + 400)
    camera.lookAt(new THREE.Vector3(mySled.position.x, 0, mySled.position.y))

    renderState()
    stats.end()
  }

  def gameActive(): Boolean = GameState.mySledId.isDefined

  def renderState(): Unit = {
    if (gameActive()) renderer.render(scene, camera)
  }

  private val playfieldAnimation = new Animation(animate)

  /** Update the client's playfield objects and draw the new playfield to the screen */
  private def animate(timestamp: Double): Unit = {
    val deltaSeconds = nextTimeSlice()
    val newState     = nextState(math.max(deltaSeconds, 0))

    drawPlayfield(
      newState.snowballs,
      newState.sleds,
      newState.mySled,
      newState.trees,
      newState.playfield
    )
  }

  def startRedraw(): Unit = playfieldAnimation.start()

  def stopRedraw(): Unit = playfieldAnimation.cancel()

  window.addEventListener(
    "resize", { _: Event =>
      camera.aspect = math.min(getWidth / getHeight, 3)
      camera.updateProjectionMatrix()

      if (gameActive() && playfieldAnimation.animating()) ClientMain.resize()
      renderState()
    }
  )
}
