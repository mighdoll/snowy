package snowy.client

import minithree.THREE
import minithree.THREE.{Object3D, Vector3}
import minithree.raw.MeshPhongMaterialParameters
import snowy.GameClientProtocol.Scoreboard
import snowy.client.ThreeMain._
import snowy.draw.{AddGrid, UpdateSleds, UpdateSnowballs, UpdateTrees}
import snowy.playfield.{Sled, Snowball, Store, Tree}
import vector.Vec2d

import scala.scalajs.js.Dynamic

object DrawState {
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
  val amb   = new THREE.AmbientLight(0x888888)
  val light = new THREE.DirectionalLight(0xffffff)

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

  def setup(): Unit = {
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
    val myPos = new Vector3(mySled.pos.x, 0, mySled.pos.y)
    UpdateTrees.updateCtrees(trees.items, myPos)
    UpdateSnowballs.updateCsnowballs(snowballs.items, myPos)
    UpdateSleds.updateCsleds(sleds.items, mySled)

    camera.position.x = mySled._position.x
    camera.position.z = mySled._position.y + 400
    camera.lookAt(new THREE.Vector3(mySled._position.x, 0, mySled._position.y))

    renderer.render(scene, camera)
  }

  def removeAll(): Unit = {
    // TODO consider creeating your own list of things added to the scene
    // so that you don't have to carefully match what you add with what you remove
    scene.remove(amb)
    scene.remove(light)
    scene.remove(Groups.threeTrees)
    scene.remove(Groups.threeSnowballs)
    scene.remove(Groups.threeSleds)
    Groups.threeGrid.foreach { grid =>
      scene.remove(grid)
    }
  }
}
