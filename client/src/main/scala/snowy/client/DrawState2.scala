package snowy.client

import minithree.THREE
import minithree.THREE.Object3D
import minithree.raw.MeshPhongMaterialParameters
import snowy.GameClientProtocol.Scoreboard
import snowy.client.CDraw2._
import snowy.playfield.PlayId.SledId
import snowy.playfield.{Sled, Snowball, Store, Tree}
import vector.Vec2d

import scala.scalajs.js.Dynamic

object DrawState2 {
  object Geos {
    val sledGeo   = new THREE.BoxGeometry(2, 2, 2)
    val turretGeo = new THREE.BoxGeometry(4, 4, 20)
    val skiGeo    = new THREE.BoxGeometry(0.25, 0.125, 3 - 0.25)
    val skiTipGeo = new THREE.BoxGeometry(0.25, 0.125, 0.25)

    val trunkGeo  = new THREE.BoxGeometry(10, 40, 10)
    val leave1Geo = new ConeGeometry(32, 32, 4, 1, false, 0.783, Math.PI * 2)
    val leave2Geo = new ConeGeometry(24, 16, 4, 1, false, 0.8, Math.PI * 2)

    val snowballGeo = new THREE.BoxGeometry(2, 2, 2)

    val healthLeft   = new THREE.CylinderGeometry(3.2, 3.2, 2, 12)
    val healthMiddle = new THREE.BoxGeometry(64, 2, 6.4)

    val healthLeft2   = new THREE.CylinderGeometry(1.6, 1.6, 4, 12)
    val healthMiddle2 = new THREE.BoxGeometry(64, 4, 3.2)
  }
  object Mats {
    val sledMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x2194ce, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val turretMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x222222, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val skiMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x222222, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val skiTipMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0xEE2222, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val trunkMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(
          color = 0x502A2A,
          shading = THREE.FlatShading
        )
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val leave1Mat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(
          color = 0x658033,
          shading = THREE.FlatShading
        )
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val leave2Mat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(
          color = 0x81A442,
          shading = THREE.FlatShading
        )
        .asInstanceOf[MeshPhongMaterialParameters]
    )

    val snowballMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(
          color = 0x222222,
          shading = THREE.FlatShading
        )
        .asInstanceOf[MeshPhongMaterialParameters]
    )

    val healthBorderMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(
          color = 0x444444
        )
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val healthColorMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(
          color = 0x21ce3a
        )
        .asInstanceOf[MeshPhongMaterialParameters]
    )
  }
  object Meshes {
    val mainBody = new THREE.Mesh(Geos.sledGeo, Mats.sledMat)
    val ski1     = new THREE.Mesh(Geos.skiGeo, Mats.skiMat)
    val skiTip1  = new THREE.Mesh(Geos.skiTipGeo, Mats.skiTipMat)
    val ski2     = Meshes.ski1.clone()
    val skiTip2  = Meshes.skiTip1.clone()

    val turret = new THREE.Mesh(Geos.turretGeo, Mats.turretMat)

    val trunk  = new THREE.Mesh(Geos.trunkGeo, Mats.trunkMat)
    val leave1 = new THREE.Mesh(Geos.leave1Geo, Mats.leave1Mat)
    val leave2 = new THREE.Mesh(Geos.leave2Geo, Mats.leave2Mat)

    val snowball = new THREE.Mesh(Geos.snowballGeo, Mats.snowballMat)

    val healthLeft   = new THREE.Mesh(Geos.healthLeft, Mats.healthBorderMat)
    val healthRight  = healthLeft.clone()
    val healthMiddle = new THREE.Mesh(Geos.healthMiddle, Mats.healthBorderMat)

    val healthLeftIn   = new THREE.Mesh(Geos.healthLeft2, Mats.healthColorMat)
    val healthRightIn  = healthLeftIn.clone()
    val healthMiddleIn = new THREE.Mesh(Geos.healthMiddle2, Mats.healthColorMat)
  }
  object Bodies {
    val sled   = new THREE.Object3D()
    val tree   = new THREE.Object3D()
    val health = new THREE.Object3D()
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

  Meshes.trunk.position.y = 20
  Meshes.leave1.position.y = 56
  Meshes.leave2.position.y = 64

  Bodies.tree.add(Meshes.trunk)
  for(i <- 1 to 10){
    val size= math.random()*20+10
  val leaveNGeo = new THREE.BoxGeometry(size,size,size)
  val leaveN = new THREE.Mesh(leaveNGeo, Mats.leave1Mat)
  leaveN.position.y = math.random()*100+20
  if(math.random()>0.5){
  leaveN.position.x = (math.random()*50-25)*(120-leaveN.position.y)/120*3
  }else{
  leaveN.position.z = (math.random()*50-25)*(120-leaveN.position.y)/120*3
  }
  Bodies.tree.add(leaveN)
  }
  //Bodies.tree.add(Meshes.leave1)
  //Bodies.tree.add(Meshes.leave2)

  Meshes.healthLeft.position.x = -32 + 2
  Meshes.healthRight.position.x = 32 - 2

  Meshes.healthLeftIn.position.x = (-32 + 2) * 0.9
  Meshes.healthRightIn.position.x = (32 - 2) * 0.9

  Meshes.healthLeftIn.scale.set(0.9, 1, 0.9)
  Meshes.healthMiddleIn.scale.set(0.9, 1, 0.9)
  Meshes.healthRightIn.scale.set(0.9, 1, 0.9)

  Bodies.health.add(Meshes.healthLeft)
  Bodies.health.add(Meshes.healthMiddle)
  Bodies.health.add(Meshes.healthRight)

  Bodies.health.add(Meshes.healthLeftIn)
  Bodies.health.add(Meshes.healthMiddleIn)
  Bodies.health.add(Meshes.healthRightIn)

  //Bodies.health.scale.set(0.4, 1, 0.4)

  scene.add(Bodies.health)

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
        val addTree: Object3D = Bodies.tree.clone()
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
    sleds.items.foreach { sled1 =>
      //TODO: Make this functional
      var idExists = false
      Groups.csleds.children.zipWithIndex.foreach {
        case (aSnowball, index) =>
          if (aSnowball.name == sled1.id.id.toString) {
            idExists = true
            Groups.csleds
              .children(index)
              .children(1)
              .setRotationFromAxisAngle(
                new THREE.Vector3(0, 1, 0),
                sled1.rotation
              )
            Groups.csleds.children(index).children(1).scale.x = sled1.radius
            Groups.csleds.children(index).children(1).scale.y = sled1.radius
            Groups.csleds.children(index).children(1).scale.z = sled1.radius

            Groups.csleds.children(index).position.x = sled1._position.x
            Groups.csleds.children(index).position.z = sled1._position.y
            Groups.csleds
              .children(index)
              .children(0)
              .setRotationFromAxisAngle(
                new THREE.Vector3(0, 1, 0),
                -sled1.turretRotation
              )
            Groups.csleds
              .children(index)
              .children(0)
              .position
              .set(
                math.sin(-sled1.turretRotation) * sled1.radius,
                0,
                math.cos(-sled1.turretRotation) * sled1.radius
              )
          }
      }

      if (!idExists) {
        val addSled: Object3D = new THREE.Object3D()
        val body: Object3D    = Meshes.mainBody.clone()
        val tur: Object3D     = Meshes.turret.clone()
        addSled.add(tur)
        addSled.add(body)

        addSled.position.x = sled1._position.x
        addSled.position.z = sled1._position.y
        addSled.name = sled1.id.id.toString
        Groups.csleds.add(addSled)
      }
    }

    Bodies.health.position.set(mySled._position.x, -40, mySled._position.y+40)
    Bodies.health.setRotationFromAxisAngle(new THREE.Vector3(1, 0, 0), 0.1 * math.Pi)

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

    renderer.setSize(width, height)
    renderer.render(scene, camera)
  }
  def removeSleds(deaths: Seq[SledId]): Unit = {
    val ids = deaths.map(_.id.toString)
    Groups.csleds.children = Groups.csleds.children.filter { sled =>
      !ids.contains(sled.name)
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
