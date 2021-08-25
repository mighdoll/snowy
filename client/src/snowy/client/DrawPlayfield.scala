package snowy.client

import minithree.THREE
import minithree.THREE._
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{document, window}
import snowy.GameConstants.oldPlayfieldSize
import snowy.client.ClientMain.{getHeight, getWidth}
import snowy.draw._
import snowy.playfield._
import vector.Vec2d

import scala.collection.mutable

class UpdateGroup[A](val group: Object3D) {
  val map: mutable.HashMap[PlayId[A], Object3D] = {
    val items = group.children.map(item => new PlayId[A](item.name.toInt) -> item).toSeq
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

  /** if the object's position is closer to the wrapped side
    * returns the position with */
  def playfieldWrap(obj: Object3D, pos: Vec2d, mySled: Vector3): Unit = {

    /** Return coord or wrapped coord depending on which is closer to center */
    def wrapAxis(coord: Double, center: Double, max: Double): Double = {
      ((coord - center + max / 2) % max + max) % max + center - max / 2
    }

    obj.position.x = wrapAxis(pos.x, mySled.x, oldPlayfieldSize.x)
    obj.position.z = wrapAxis(pos.y, mySled.z, oldPlayfieldSize.y)
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
}

class DrawPlayfield(renderer: WebGLRenderer,
                    val threeSleds: ThreeSleds,
                    val threeSnowballs: ThreeSnowballs,
                    val threePowerups: ThreePowerups) {
  val scene = new THREE.Scene()
  val camera =
    new THREE.PerspectiveCamera(45, math.min(getWidth() / getHeight(), 3), 1, 5000)

  val amb   = new THREE.AmbientLight(0xFFFFFF, 0.5)
  val light = new THREE.DirectionalLight(0xFFFFFF, 0.5)

  val stats = new Stats()

  private def setup(): Unit = {
    stats.showPanel(0)
    document.body.appendChild(stats.dom)

    light.position.set(0, 2, 1)

    scene.add(amb)
    scene.add(light)

    scene.add(CreateGrid.newGrid())

    scene.add(ThreeTrees.treeGroup.group)
    scene.add(threeSnowballs.snowballGroup.group)
    scene.add(threeSleds.sledGroup.group)
    scene.add(threePowerups.powerupGroup.group)
  }

  setup()

  /** Update the positions of all the three playfield items, then draw to screen */
  def drawPlayfield(snowballs: Set[Snowball],
                    sleds: Set[Sled],
                    mySled: Sled,
                    trees: Set[Tree],
                    border: Vec2d): Unit = {
    stats.begin()
    val myPos = new Vector3(mySled.position.x, 2.9 * mySled.radius, mySled.position.y)
    ThreeTrees.updateThreeTrees(trees, myPos)
    threeSnowballs.updateThreeSnowballs(snowballs, myPos)
    threeSleds.updateThreeSleds(sleds, mySled)
    threePowerups.wrapThreePowerups(myPos)

    camera.position.set(myPos.x, 1000, myPos.z + 300)
    camera.lookAt(myPos)

    renderState()
    stats.end()
  }

  def renderState(): Unit = {
    if (ClientMain.gameScreenActive) renderer.render(scene, camera)
  }

  window.addEventListener(
    "resize", { _: Event =>
      camera.aspect = math.min(getWidth() / getHeight(), 3)
      camera.updateProjectionMatrix()

      if (ClientMain.gameScreenActive) ClientMain.resize()
      renderState()
    }
  )
}
