package snowy.client

import minithree.THREE
import minithree.THREE.{MeshPhongMaterialParameters, Object3D}

import scala.scalajs.js.Dynamic

object CreateTree {
  val trunkGeo = new THREE.BoxGeometry(10, 200, 10)

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

  def randBetween(max: Double, min: Double): Double = math.random() * (max - min) + min

  /** @return a three tree with a random shape at position (0, 0) */
  def randomTree(): Object3D = {
    val tree = new THREE.Object3D

    val trunk = new THREE.Mesh(trunkGeo, trunkMat)
    trunk.position.y = 100
    tree.add(trunk)

    val topSize = randBetween(50, 30)
    val topGeo  = new THREE.BoxGeometry(topSize, topSize, topSize)
    val treeTop = new THREE.Mesh(topGeo, leafMat)
    treeTop.position.y = 200
    tree.add(treeTop)

    val leafBlocksPerTree = 5
    val leafSizeMax       = 50
    val leafSizeMin       = 20
    val leafHeightMax     = 200
    val leafHeightMin     = 50
    for (_ <- 1 to leafBlocksPerTree) {
      val size        = randBetween(leafSizeMax, leafSizeMin)
      val leaveHeight = randBetween(leafHeightMax, leafHeightMin)

      val offsetDirection  = math.round(math.random) * 2 - 1 // Either 1 or -1
      val offsetFromTrunk  = offsetDirection * (leafHeightMax - leaveHeight) / 2
      val offsetFromBranch = math.random * size - size / 2

      val leaveNGeo = new THREE.BoxGeometry(size, size, size)
      val leaveN    = new THREE.Mesh(leaveNGeo, leafMat)

      val branchGeo = new THREE.BoxGeometry(2, 2, 2)
      val branch    = new THREE.Mesh(branchGeo, trunkMat)

      leaveN.position.y = leaveHeight
      branch.position.y = leaveHeight

      if (math.random() > 0.5) {
        leaveN.position.z = offsetFromBranch
        leaveN.position.x = offsetFromTrunk

        branch.scale.x = math.abs(offsetFromTrunk) / 2

        branch.position.x = offsetFromTrunk / 2
        tree.add(branch)
      } else {
        leaveN.position.x = offsetFromBranch
        leaveN.position.z = offsetFromTrunk

        branch.scale.z = math.abs(offsetFromTrunk) / 2

        branch.position.z = offsetFromTrunk / 2
        tree.add(branch)
      }
      tree.add(leaveN)
    }

    tree
  }
}
