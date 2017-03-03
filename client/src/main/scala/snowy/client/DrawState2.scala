package snowy.client

import minithree.THREE
import minithree.THREE.Object3D
import minithree.raw.MeshPhongMaterialParameters
import snowy.GameClientProtocol.Scoreboard
import snowy.client.CDraw2._
import snowy.playfield.{Sled, Snowball, Store, Tree}
import vector.Vec2d

import scala.scalajs.js
import scala.scalajs.js.Dynamic

object DrawState2 {
  var sledGeo = new THREE.IcosahedronGeometry(5, 0)
  var sledMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(color = 0x2194ce, shading = THREE.FlatShading)
      .asInstanceOf[MeshPhongMaterialParameters]
  )

  var amb   = new THREE.AmbientLight(0x888888)
  var light = new THREE.DirectionalLight(0xffffff)
  var grid  = new THREE.GridHelper(4000, 10)

  var ctrees = new THREE.Object3D()
  var sled   = new THREE.Mesh(sledGeo, sledMat)

  def setup(): Unit = {
    scene.add(amb)

    light.position.set(10, 20, 0)
    scene.add(light)

    println("works")

    sled.position.set(200, 100, -2.5)
    scene.add(sled)

    camera.position.x = sled.position.x
    camera.position.z = sled.position.z - 100
    camera.position.y = 300
    camera.lookAt(sled.position)

    scene.add(grid)

    scene.add(ctrees)
  }

  var trunkGeo  = new THREE.BoxGeometry(8, 40, 8)
  var leave1Geo = new ConeGeometry(16, 32, 4, 1, false, 0.783, Math.PI * 2)
  var leave2Geo = new ConeGeometry(12, 16, 4, 1, false, 0.8, Math.PI * 2)

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

  var trunk = new THREE.Mesh(trunkGeo, trunkMat)
  var leave1 = new THREE.Mesh(leave1Geo, leave1Mat)
  var leave2 = new THREE.Mesh(leave2Geo, leave2Mat)

  trunk.position.y = 20;
  leave1.position.y = 56;
  leave2.position.y = 64;

  var tree = new THREE.Object3D()
  tree.add(trunk)
  tree.add(leave1)
  tree.add(leave2)

  def drawState(snowballs: Store[Snowball],
                sleds: Store[Sled],
                mySled: Sled,
                trees: Store[Tree],
                border: Vec2d,
                scoreboard: Scoreboard): Unit = {
    trees.items.map { tree1 =>
      var addTree: Object3D = tree.clone()
      addTree.position.x = tree1.pos.x
      addTree.position.z = tree1.pos.y
      ctrees.add(addTree)
    }

    sled.position.set(mySled._position.x, -2.5, mySled._position.y)

    camera.position.x = sled.position.x
    camera.position.z = sled.position.z + 100
    camera.lookAt(sled.position)

    renderer.setSize(width, height)
    renderer.render(scene, camera)

    //TODO: Modify trees instead of recreating them
    ctrees.children = js.Array[minithree.raw.Object3D]()
  }

}
