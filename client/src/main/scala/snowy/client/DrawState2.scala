package snowy.client

import minithree.THREE
import minithree.THREE.Object3D
import minithree.raw.MeshPhongMaterialParameters
import snowy.GameClientProtocol.Scoreboard
import snowy.client.CDraw2._
import snowy.playfield.{Sled, Snowball, Store, Tree}
import vector.Vec2d

import scala.scalajs.js.Dynamic

object DrawState2 {
  var sledGeo = new THREE.BoxGeometry(2, 2, 2)
  var sledMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(color = 0x2194ce, shading = THREE.FlatShading)
      .asInstanceOf[MeshPhongMaterialParameters]
  )

  var turretGeo = new THREE.BoxGeometry(4, 4, 20)
  var turretMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(color = 0x222222, shading = THREE.FlatShading)
      .asInstanceOf[MeshPhongMaterialParameters]
  )
  var skiGeo = new THREE.BoxGeometry(0.25, 0.125, 3-0.25)
  var skiTipGeo = new THREE.BoxGeometry(0.25, 0.125, 0.25)
  
  var skiMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(color = 0x222222, shading = THREE.FlatShading)
      .asInstanceOf[MeshPhongMaterialParameters]
  )

  var skiTipMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(color = 0xEE2222, shading = THREE.FlatShading)
      .asInstanceOf[MeshPhongMaterialParameters]
  )

  var amb   = new THREE.AmbientLight(0x888888)
  var light = new THREE.DirectionalLight(0xffffff)
  var grid  = new THREE.GridHelper(4000, 50)

  var ctrees   = new THREE.Object3D()
  var sled     = new THREE.Object3D()
  var mainBody = new THREE.Mesh(sledGeo, sledMat)
  var ski1 = new THREE.Mesh(skiGeo, skiMat)
  var skiTip1 = new THREE.Mesh(skiTipGeo, skiTipMat)
  var turret   = new THREE.Mesh(turretGeo, turretMat)
  turret.translate(-5, new THREE.Vector3(0, 0, 1))
  sled.add(mainBody)
  sled.add(turret)
  skiTip1.translate(1, new THREE.Vector3(0, 0, 1.5-0.25/2))
  ski1.add(skiTip1)
  
  ski1.translate(1, new THREE.Vector3(0, 0, -0.5/2))
  var ski2=ski1.clone()
  ski1.translate(5, new THREE.Vector3(-0.25, -0.5, 0))
  ski2.translate(5, new THREE.Vector3(0.25, -0.5, 0))
  

  mainBody.add(ski1)
  mainBody.add(ski2)
  var trunkGeo  = new THREE.BoxGeometry(10, 40, 10)
  var leave1Geo = new ConeGeometry(32, 32, 4, 1, false, 0.783, Math.PI * 2)
  var leave2Geo = new ConeGeometry(24, 16, 4, 1, false, 0.8, Math.PI * 2)
  var trunkMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(
        color = 0x502A2A,
        shading = THREE.FlatShading
      )
      .asInstanceOf[MeshPhongMaterialParameters]
  )
  var leave1Mat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(
        color = 0x658033,
        shading = THREE.FlatShading
      )
      .asInstanceOf[MeshPhongMaterialParameters]
  )
  var leave2Mat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(
        color = 0x81A442,
        shading = THREE.FlatShading
      )
      .asInstanceOf[MeshPhongMaterialParameters]
  )
  var trunk  = new THREE.Mesh(trunkGeo, trunkMat)
  var leave1 = new THREE.Mesh(leave1Geo, leave1Mat)
  var leave2 = new THREE.Mesh(leave2Geo, leave2Mat)
  var tree   = new THREE.Object3D()

  trunk.position.y = 20;
  leave1.position.y = 56;
  leave2.position.y = 64;
  var snowballMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(
        color = 0x222222,
        shading = THREE.FlatShading
      )
      .asInstanceOf[MeshPhongMaterialParameters]
  )
  tree.add(trunk)
  tree.add(leave1)
  tree.add(leave2)
  var snowballGeo = new THREE.BoxGeometry(2, 2, 2)
  var snowball    = new THREE.Mesh(snowballGeo, snowballMat)
  var csnowballs  = new THREE.Object3D()
  var csleds      = new THREE.Object3D()
  def setup(): Unit = {
    scene.add(amb)

    light.position.set(10, 20, 0)
    scene.add(light)

    println("works")

    sled.position.set(200, 100, -2.5)
    scene.add(sled)

    camera.position.x = sled.position.x
    camera.position.z = sled.position.z + 400
    camera.position.y = 800
    camera.lookAt(sled.position)

    scene.add(grid)

    scene.add(ctrees)

    scene.add(csnowballs)

    scene.add(csleds)
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
      ctrees.children.zipWithIndex.foreach {
        case (possibleTree, index) =>
          if (possibleTree.name == tree1.id.id.toString) {
            idExists = true
            ctrees.children(index).position.x = tree1.pos.x
            ctrees.children(index).position.z = tree1.pos.y
          }
      }
      if (idExists == false) {
        var addTree: Object3D = tree.clone()
        addTree.position.x = tree1.pos.x
        addTree.position.z = tree1.pos.y
        addTree.name = tree1.id.id.toString
        ctrees.add(addTree)
        println("adding tree")
      }
    }
    //TODO: Remove snowballs after dead
    snowballs.items.foreach { snowball1 =>
      //TODO: Make this functional
      var idExists = false
      csnowballs.children.zipWithIndex.foreach {
        case (aSnowball, index) =>
          if (aSnowball.name == snowball1.id.id.toString) {
            println("moving snowball")
            idExists = true
            csnowballs.children(index).position.x = snowball1._position.x
            csnowballs.children(index).position.z = snowball1._position.y
          }
      }
      if (idExists == false) {
        var addSnowball: Object3D = snowball.clone()
        addSnowball.position.x = snowball1._position.x
        addSnowball.position.z = snowball1._position.y
        addSnowball.scale.set(
          snowball1.radius,
          snowball1.radius,
          snowball1.radius
        )
        addSnowball.name = snowball1.id.id.toString
        csnowballs.add(addSnowball)
        println("adding snowball")
      }
    }
    //TODO: Remove sleds after dead
    sleds.items.foreach { sled1 =>
      //TODO: Make this functional
      var idExists = false
      csleds.children.zipWithIndex.foreach {
        case (aSnowball, index) =>
          if (aSnowball.name == sled1.id.id.toString) {
            println("moving snowball")
            idExists = true
            csleds
              .children(index)
              .children(1)
              .setRotationFromAxisAngle(
                new THREE.Vector3(0, 1, 0),
                sled1.rotation
              )
            csleds.children(index).children(1).scale.x = sled1.radius
            csleds.children(index).children(1).scale.y = sled1.radius
            csleds.children(index).children(1).scale.z = sled1.radius

            csleds.children(index).position.x = sled1._position.x
            csleds.children(index).position.z = sled1._position.y
            csleds
              .children(index)
              .children(0)
              .setRotationFromAxisAngle(
                new THREE.Vector3(0, 1, 0),
                -sled1.turretRotation
              )
            csleds
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

      if (idExists == false) {
        var addSled: Object3D = new THREE.Object3D()
        var body: Object3D    = mainBody.clone()
        var tur: Object3D     = turret.clone()
        addSled.add(tur)
        addSled.add(body)

        addSled.position.x = sled1._position.x
        addSled.position.z = sled1._position.y
        addSled.name = sled1.id.id.toString
        csleds.add(addSled)
        println("adding sled")
      }
    }

    mainBody.scale.x = mySled.radius
    mainBody.scale.y = mySled.radius
    mainBody.scale.z = mySled.radius
    mainBody.setRotationFromAxisAngle(new THREE.Vector3(0, 1, 0), mySled.rotation)
    sled.position.set(mySled._position.x, 0, mySled._position.y)
    turret.setRotationFromAxisAngle(new THREE.Vector3(0, 1, 0), -mySled.turretRotation)
    turret.position.set(
      math.sin(-mySled.turretRotation) * mySled.radius,
      0,
      math.cos(-mySled.turretRotation) * mySled.radius
    )

    camera.position.x = sled.position.x
    camera.position.z = sled.position.z + 400
    camera.lookAt(sled.position)

    renderer.setSize(width, height)
    renderer.render(scene, camera)
  }
}
