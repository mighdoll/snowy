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
    val sled   = new THREE.BoxGeometry(2, 2, 2)
    val turret = new THREE.BoxGeometry(4, 4, 20)
    val ski    = new THREE.BoxGeometry(0.25, 0.125, 3 - 0.25)
    val skiTip = new THREE.BoxGeometry(0.25, 0.125, 0.25)
    val snowball = new THREE.BoxGeometry(2, 2, 2)
    val health = new THREE.BoxGeometry(64, 4, 16)
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
    // TODO what does c-prefix mean here? rename?
    val ctrees                  = new THREE.Object3D()
    val csnowballs              = new THREE.Object3D()
    val csleds                  = new THREE.Object3D()
    var cgrid: Option[Object3D] = None
  }
  val amb   = new THREE.AmbientLight(0x888888)
  val light = new THREE.DirectionalLight(0xffffff)

  // only x and z
  // TODO what does this do? rename?
  def transformPositionMod(pos: Vector3, center: Vector3, mod: Vector3): Vector3 = {
    new Vector3(
      ((pos.x - center.x + mod.x / 2) % mod.x + mod.x) % mod.x + center.x - mod.x / 2,
      pos.y,
      ((pos.z - center.z + mod.z / 2) % mod.z + mod.z) % mod.z + center.z - mod.z / 2
    )
  }

  def setup(): Unit = {
    scene.add(amb)

    light.position.set(10, 20, 0)
    scene.add(light)

    camera.position.y = 800

    Groups.cgrid = Some(AddGrid.createGrid())
    Groups.cgrid.foreach { grid =>
      scene.add(grid)
    }

    scene.add(Groups.ctrees)
    scene.add(Groups.csnowballs)
    scene.add(Groups.csleds)

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
    scene.remove(Groups.ctrees)
    scene.remove(Groups.csnowballs)
    scene.remove(Groups.csleds)
    Groups.cgrid.foreach { grid =>
      scene.remove(grid)
    }
  }
}
