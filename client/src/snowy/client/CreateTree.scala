package snowy.client

import minithree.THREE
import minithree.THREE.{MeshLambertMaterialParameters, Object3D}

import scala.scalajs.js.Dynamic

object CreateTree {
  val trunkGeo = new THREE.BoxGeometry(10, 200, 10)

  val trunkMat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0x502a2a)
      .asInstanceOf[MeshLambertMaterialParameters]
  )
  val leafMat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0x658033)
      .asInstanceOf[MeshLambertMaterialParameters]
  )
  val leaf2Mat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0x81a442)
      .asInstanceOf[MeshLambertMaterialParameters]
  )
  val snowMat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0xeeeeee)
      .asInstanceOf[MeshLambertMaterialParameters]
  )

  def randBetween(max: Double, min: Double): Double = math.random() * (max - min) + min

  /** @return a three tree with a random shape at position (0, 0) */
  def randomTree(): Object3D = {
    val tree = new THREE.Object3D

    val leafBlocksPerTree = 3
    val leafSizeMax       = 20
    val leafSizeMin       = 10
    val leafHeightMax     = 200
    val leafHeightMin     = 50
    val snowThickness     = 5
    val snowMinHeight     = 3

    val trunk = new THREE.Mesh(trunkGeo, trunkMat)
    trunk.position.y = 100
    tree.add(trunk)

    val topSize = randBetween(30, 20)
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
    snowTop.position.y = treeTop.position.y + topSize / 2
    tree.add(snowTop)

    for (_ <- 1 to leafBlocksPerTree) {
      val size        = randBetween(leafSizeMax, leafSizeMin)
      val leaveHeight = randBetween(leafHeightMax, leafHeightMin)

      val offsetDirection  = math.round(math.random()) * 2 - 1 // Either 1 or -1
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

      val branchAxis = math.random() > 0.5
      val oppositeVec =
        if (branchAxis) new THREE.Vector3(0, 0, 1) else new THREE.Vector3(1, 0, 0)
      val axisVec =
        if (!branchAxis) new THREE.Vector3(0, 0, 1) else new THREE.Vector3(1, 0, 0)

      leaveN.position.add(oppositeVec.clone().multiplyScalar(offsetFromBranch))
      leaveN.position.add(axisVec.clone().multiplyScalar(offsetFromTrunk))

      branch.scale.add(axisVec.clone().multiplyScalar(math.abs(offsetFromTrunk) / 2))
      branch.position.add(axisVec.clone().multiplyScalar(offsetFromTrunk / 2))

      snowOnLeaf.position.set(
        leaveN.position.x,
        leaveHeight + size / 2,
        leaveN.position.z
      )

      tree.add(branch)
      tree.add(leaveN)
      tree.add(snowOnLeaf)
    }

    tree
  }
}
