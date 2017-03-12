package snowy.client

import minithree.THREE
import minithree.THREE.Object3D
import minithree.raw.MeshPhongMaterialParameters
import snowy.GameClientProtocol.Scoreboard
import snowy.client.ThreeMain._
import snowy.playfield.PlayId.{BallId, SledId}
import snowy.playfield.{Sled, Snowball, Store, Tree}
import vector.Vec2d

import scala.scalajs.js.Dynamic

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
    val ctrees     = new THREE.Object3D()
    val csnowballs = new THREE.Object3D()
    val csleds     = new THREE.Object3D()
  }

  val amb   = new THREE.AmbientLight(0x888888)
  val light = new THREE.DirectionalLight(0xffffff)
  val grid  = new THREE.GridHelper(4000, 50)

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

    scene.add(grid)

    scene.add(Groups.ctrees)

    scene.add(Groups.csnowballs)

    scene.add(Groups.csleds)

  }
  //TODO: Remove trees after dead
  def drawState(snowballs: Store[Snowball],
                sleds: Store[Sled],
                mySled: Sled,
                trees: Store[Tree],
                border: Vec2d,
                scoreboard: Scoreboard): Unit = {
    trees.items.foreach { tree1 =>
      //TODO: Make this functional
      var idExists = false
      Groups.ctrees.children.zipWithIndex.foreach {
        case (possibleTree, index) =>
          if (possibleTree.name == tree1.id.id.toString) {
            idExists = true
            Groups.ctrees.children(index).position.x = tree1.pos.x
            Groups.ctrees.children(index).position.z = tree1.pos.y
          }
      }
      if (!idExists) {
        val addTree: Object3D = ThreeTree.randomTree()
        addTree.position.x = tree1.pos.x
        addTree.position.z = tree1.pos.y
        addTree.name = tree1.id.id.toString
        Groups.ctrees.add(addTree)
      }
    }
    //TODO: Remove snowballs after dead
    snowballs.items.foreach { snowball1 =>
      //TODO: Make this functional
      var idExists = false
      Groups.csnowballs.children.zipWithIndex.foreach {
        case (aSnowball, index) =>
          if (aSnowball.name == snowball1.id.id.toString) {
            idExists = true
            Groups.csnowballs.children(index).position.x = snowball1._position.x
            Groups.csnowballs.children(index).position.z = snowball1._position.y
          }
      }
      if (!idExists) {
        val addSnowball: Object3D = Meshes.snowball.clone()
        addSnowball.position.x = snowball1._position.x
        addSnowball.position.z = snowball1._position.y
        addSnowball.scale.set(
          snowball1.radius,
          snowball1.radius,
          snowball1.radius
        )
        addSnowball.name = snowball1.id.id.toString
        Groups.csnowballs.add(addSnowball)
      }
    }
    //TODO: Remove sleds after dead
    sleds.items.filter(_.id != mySled.id).foreach { sled1 =>
      //TODO: Make this functional
      var idExists = false
      Groups.csleds.children.zipWithIndex.foreach {
        case (aSnowball, index) =>
          if (aSnowball.name == sled1.id.id.toString) {
            idExists = true
            val csled = Groups.csleds.children(index)

            csled
              .children(1)
              .setRotationFromAxisAngle(
                new THREE.Vector3(0, 1, 0),
                sled1.rotation
              )
            csled.children(1).scale.x = sled1.radius
            csled.children(1).scale.y = sled1.radius
            csled.children(1).scale.z = sled1.radius

            csled.position.x = sled1._position.x
            csled.position.z = sled1._position.y
            csled
              .children(0)
              .setRotationFromAxisAngle(
                new THREE.Vector3(0, 1, 0),
                -sled1.turretRotation
              )
            csled
              .children(0)
              .position
              .set(
                math.sin(-sled1.turretRotation) * sled1.radius,
                0,
                math.cos(-sled1.turretRotation) * sled1.radius
              )

            csled.children(2).scale.x = sled1.health / sled1.maxHealth

          }
      }

      if (!idExists) {
        val addSled: Object3D = new THREE.Object3D()
        val body: Object3D    = Meshes.mainBody.clone()
        val tur: Object3D     = Meshes.turret.clone()
        val health: Object3D  = new THREE.Mesh(Geos.health, Mats.enemyHealth)

        health.position.z = -50

        addSled.add(tur)
        addSled.add(body)
        addSled.add(health)

        addSled.position.x = sled1._position.x
        addSled.position.z = sled1._position.y
        addSled.name = sled1.id.id.toString
        Groups.csleds.add(addSled)
      }
    }

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
  def removeSleds(deaths: Seq[SledId]): Unit = {
    val ids = deaths.map(_.id.toString)
    Groups.csleds.children = Groups.csleds.children.filter { sled =>
      !ids.contains(sled.name)
    }
  }
  def removeSnowballs(deaths: Seq[BallId]): Unit = {
    val ids = deaths.map(_.id.toString)
    Groups.csnowballs.children = Groups.csnowballs.children.filter { snowball =>
      !ids.contains(snowball.name)
    }
  }
  def removeAll(): Unit = {
    scene.remove(amb)
    scene.remove(light)
    scene.remove(Bodies.sled)
    scene.remove(grid)
    scene.remove(Groups.ctrees)
    scene.remove(Groups.csnowballs)
    scene.remove(Groups.csleds)
  }
}
