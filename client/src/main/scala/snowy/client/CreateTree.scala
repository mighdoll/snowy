package snowy.client

import minithree.THREE
import minithree.THREE.{MeshLambertMaterialParameters, Object3D}

import scala.scalajs.js.Dynamic

object CreateTree {
  val trunkGeo = new THREE.BoxGeometry(10, 200, 10)

  val trunkMat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0x502A2A)
      .asInstanceOf[MeshLambertMaterialParameters]
  )
  val leafMat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0x658033)
      .asInstanceOf[MeshLambertMaterialParameters]
  )
  val leaf2Mat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0x81A442)
      .asInstanceOf[MeshLambertMaterialParameters]
  )
  val snowMat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0xEEEEEE)
      .asInstanceOf[MeshLambertMaterialParameters]
  )

  def randBetween(max: Double, min: Double): Double = math.random() * (max - min) + min

  /** @return a three tree with a random shape at position (0, 0) */
  def randomTree(): Object3D = {
    val tree = new THREE.Object3D

    val leafBlocksPerTree = 5
    val leafSizeMax       = 50
    val leafSizeMin       = 20
    val leafHeightMax     = 200
    val leafHeightMin     = 50
    val snowThickness     = 5
    val snowMinHeight     = 20

    val trunk = new THREE.Mesh(trunkGeo, trunkMat)
    trunk.position.y = 100
    tree.add(trunk)

    val topSize = randBetween(50, 30)
    val topGeo  = new THREE.BoxGeometry(topSize, topSize, topSize)
    val treeTop = new THREE.Mesh(topGeo, leaf2Mat)
    treeTop.position.y = 200
    tree.add(treeTop)

    val snowTopGeo = new THREE.BoxGeometry(
      topSize + snowThickness * 2,
      randBetween(topSize, snowMinHeight),
      topSize + snowThickness * 2
    )
    val snowTop = new THREE.Mesh(snowTopGeo, snowMat)
    snowTop.position.y = treeTop.position.y + topSize
    tree.add(snowTop)

    for (_ <- 1 to leafBlocksPerTree) {
      val size        = randBetween(leafSizeMax, leafSizeMin)
      val leaveHeight = randBetween(leafHeightMax, leafHeightMin)

      val offsetDirection  = math.round(math.random) * 2 - 1 // Either 1 or -1
      val offsetFromTrunk  = offsetDirection * (leafHeightMax - leaveHeight) / 2
      val offsetFromBranch = randBetween(size / 2, -size / 2)

      val leaveNGeo = new THREE.BoxGeometry(size, size, size)
      val leaveN    = new THREE.Mesh(leaveNGeo, leafMat)

      val snowGeo = new THREE.BoxGeometry(
        size + snowThickness * 2,
        randBetween(size, snowMinHeight),
        size + snowThickness * 2
      )
      val snowOnLeaf = new THREE.Mesh(snowGeo, snowMat)

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
      snowOnLeaf.position.set(
        leaveN.position.x,
        leaveHeight + size,
        leaveN.position.z
      )

      tree.add(leaveN)
      tree.add(snowOnLeaf)
    }

    tree
  }
}
