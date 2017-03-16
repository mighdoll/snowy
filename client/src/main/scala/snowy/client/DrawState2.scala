package snowy.client

import minithree.THREE
import minithree.THREE.{Object3D, Vector3}
import minithree.raw.{LineBasicMaterialParameters, MeshPhongMaterialParameters}
import snowy.GameClientProtocol.Scoreboard
import snowy.GameConstants
import snowy.client.ThreeMain._
import snowy.draw.{UpdateSleds, UpdateSnowballs, UpdateTrees}
import snowy.playfield.PlayId.{BallId, SledId}
import snowy.playfield.{Sled, Snowball, Store, Tree}
import vector.Vec2d

import scala.scalajs.js
import scala.scalajs.js.Dynamic
import scala.scalajs.js.typedarray.Float32Array

object DrawState2 {
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
  object Meshes {
    val mainBody = new THREE.Mesh(Geos.sled, Mats.sled)
    val turret   = new THREE.Mesh(Geos.turret, Mats.turret)
    val ski1     = new THREE.Mesh(Geos.ski, Mats.ski)
    val skiTip1  = new THREE.Mesh(Geos.skiTip, Mats.skiTip)
    val ski2     = Meshes.ski1.clone()
    val skiTip2  = Meshes.skiTip1.clone()

    val snowball = new THREE.Mesh(Geos.snowball, Mats.snowball)

    val health = new THREE.Mesh(Geos.health, Mats.healthColor)
  }
  object Bodies {
    val sled = new THREE.Object3D()
    val tree = ThreeTree.randomTree()
  }
  object Groups {
    val ctrees                  = new THREE.Object3D()
    val csnowballs              = new THREE.Object3D()
    val csleds                  = new THREE.Object3D()
    var cgrid: Option[Object3D] = None
  }
  val amb   = new THREE.AmbientLight(0x888888)
  val light = new THREE.DirectionalLight(0xffffff)

  Meshes.turret.position.set(0, 0, -5)
  Bodies.sled.add(Meshes.mainBody)
  Bodies.sled.add(Meshes.turret)

  Meshes.skiTip1.position.set(0, 0, 1.5 - 0.25 / 2)
  Meshes.ski1.add(Meshes.skiTip1)

  Meshes.skiTip2.position.set(0, 0, 1.5 - 0.25 / 2)
  Meshes.ski2.add(Meshes.skiTip2)

  Meshes.ski1.position.set(-1.25, -2.5, 0)
  Meshes.ski2.position.set(1.25, -2.5, 0)

  Meshes.mainBody.add(Meshes.ski1)
  Meshes.mainBody.add(Meshes.ski2)

  scene.add(Meshes.health)

  // only x and z
  def transformPositionMod(pos: Vector3, center: Vector3, mod: Vector3): Vector3 = {
    new Vector3(
      ((pos.x - center.x + mod.x / 2) % mod.x + mod.x) % mod.x + center.x - mod.x / 2,
      pos.y,
      ((pos.z - center.z + mod.z / 2) % mod.z + mod.z) % mod.z + center.z - mod.z / 2
    )
  }
  def addGrid(): Object3D = {
    val xSpacing = 50
    val ySpacing = 50

    val xAmount = math.ceil(GameConstants.playfield.x / xSpacing).toInt
    val yAmount = math.ceil(GameConstants.playfield.y / ySpacing).toInt

    val material = new THREE.LineBasicMaterial(
      Dynamic
        .literal(color = 0x0)
        .asInstanceOf[LineBasicMaterialParameters]
    )

    val grid = new THREE.Object3D()
    for (i <- 0 to xAmount) {
      val geo = new THREE.BufferGeometry()
      val vertices = new Float32Array(
        js.Array(
          i * xSpacing,
          0,
          yAmount * ySpacing,
          i * xSpacing,
          0,
          0
        )
      )
      geo.addAttribute("position", new THREE.BufferAttribute(vertices, 3))
      val line = new THREE.Line(geo, material)
      grid.add(line)
    }

    for (j <- 0 to yAmount) {
      val geo = new THREE.BufferGeometry()
      val vertices = new Float32Array(
        js.Array(
          0,
          0,
          j * ySpacing,
          xAmount * xSpacing,
          0,
          j * ySpacing
        )
      )
      geo.addAttribute("position", new THREE.BufferAttribute(vertices, 3))
      val line = new THREE.Line(geo, material)
      grid.add(line)
    }
    grid
  }
  def setup(): Unit = {
    scene.add(amb)

    light.position.set(10, 20, 0)
    scene.add(light)

    Bodies.sled.position.set(200, 100, -2.5)
    scene.add(Bodies.sled)

    camera.position.x = Bodies.sled.position.x
    camera.position.z = Bodies.sled.position.z + 400
    camera.position.y = 800
    camera.lookAt(Bodies.sled.position)

    Groups.cgrid = Some(addGrid())
    Groups.cgrid.foreach { grid =>
      scene.add(grid)
    }

    scene.add(Groups.ctrees)
    scene.add(Groups.csnowballs)
    scene.add(Groups.csleds)

  }

  def drawState(snowballs: Store[Snowball],
                sleds: Store[Sled],
                mySled: Sled,
                trees: Store[Tree],
                border: Vec2d,
                scoreboard: Scoreboard): Unit = {
    UpdateTrees.updateCtrees(trees.items)
    UpdateSnowballs.updateCsnowballs(snowballs.items)
    UpdateSleds.updateCsleds(sleds.items.filter(_.id != mySled.id))

    Meshes.health.position.set(mySled._position.x, 0, mySled._position.y - 50)
    Meshes.health.scale.x = mySled.health / mySled.maxHealth

    Meshes.mainBody.scale.x = mySled.radius
    Meshes.mainBody.scale.y = mySled.radius
    Meshes.mainBody.scale.z = mySled.radius
    Meshes.mainBody.setRotationFromAxisAngle(new THREE.Vector3(0, 1, 0), mySled.rotation)
    Bodies.sled.position.set(mySled._position.x, 0, mySled._position.y)
    Meshes.turret
      .setRotationFromAxisAngle(new THREE.Vector3(0, 1, 0), -mySled.turretRotation)
    Meshes.turret.position.set(
      math.sin(-mySled.turretRotation) * mySled.radius,
      0,
      math.cos(-mySled.turretRotation) * mySled.radius
    )

    camera.position.x = Bodies.sled.position.x
    camera.position.z = Bodies.sled.position.z + 400
    camera.lookAt(Bodies.sled.position)

    renderer.render(scene, camera)
  }

  def removeAll(): Unit = {
    scene.remove(amb)
    scene.remove(light)
    scene.remove(Bodies.sled)
    scene.remove(Groups.ctrees)
    scene.remove(Groups.csnowballs)
    scene.remove(Groups.csleds)
    Groups.cgrid.foreach { grid =>
      scene.remove(grid)
    }
  }
}
