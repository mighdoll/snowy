package snowy.client

import minithree.THREE
import minithree.raw.{MeshPhongMaterialParameters, Object3D}

import scala.scalajs.js.Dynamic

object ThreeTree {
  val trunkGeo  = new THREE.BoxGeometry(10, 200, 10)

  val trunkMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(
        color = 0x502A2A,
        shading = THREE.FlatShading
      )
      .asInstanceOf[MeshPhongMaterialParameters]
  )
  val leafMat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(
        color = 0x658033,
        shading = THREE.FlatShading
      )
      .asInstanceOf[MeshPhongMaterialParameters]
  )
  val leaf2Mat = new THREE.MeshPhongMaterial(
    Dynamic
      .literal(
        color = 0x81A442,
        shading = THREE.FlatShading
      )
      .asInstanceOf[MeshPhongMaterialParameters]
  )
  def randomTree(): Object3D = {
    var tree = new THREE.Object3D

    val trunk = new THREE.Mesh(trunkGeo, trunkMat)
    trunk.position.y = 100
    tree.add(trunk)


    val topSize = math.random() * 20 + 30
    val topGeo  = new THREE.BoxGeometry(topSize, topSize, topSize)
    val treeTop = new THREE.Mesh(topGeo, leafMat)
    treeTop.position.y = 200
    tree.add(treeTop)


    for (_ <- 1 to 5) {
      val size      = math.random() * 30 + 20
      val leaveNGeo = new THREE.BoxGeometry(size, size, size)
      val leaveN    = new THREE.Mesh(leaveNGeo, leafMat)
      leaveN.position.y = math.random() * 150 + 50
      if (math.random() > 0.5) {
        leaveN.position.z = math.random * size - size / 2
        leaveN.position.x = (math.round(math.random) * 2 - 1) * (200 - leaveN.position.y) / 2

        val branchGeo = new THREE.BoxGeometry(math.abs(leaveN.position.x), 2, 2)
        val branch    = new THREE.Mesh(branchGeo, trunkMat)
        branch.position.x = leaveN.position.x / 2
        branch.position.y = leaveN.position.y
        tree.add(branch)
      } else {
        leaveN.position.x = math.random * size - size / 2
        leaveN.position.z = (math.round(math.random) * 2 - 1) * (200 - leaveN.position.y) / 2

        val branchGeo = new THREE.BoxGeometry(2, 2, math.abs(leaveN.position.z))
        val branch    = new THREE.Mesh(branchGeo, trunkMat)
        branch.position.z = leaveN.position.z / 2
        branch.position.y = leaveN.position.y
        tree.add(branch)
      }
      tree.add(leaveN)
    }

    tree
  }
}
