package snowy.draw

import minithree.THREE.Object3D
import minithree.raw.Vector3
import snowy.client.DrawPlayfield.Groups
import snowy.client.{CreateTree, DrawPlayfield, UpdateGroup}
import snowy.playfield.Tree

object ThreeTrees {
  val treeGroup = new UpdateGroup[Tree](Groups.threeTrees)
  def updateThreeTrees(trees: Set[Tree], myPos: Vector3): Unit = {
    trees.foreach { tree1 =>
      treeGroup.map.get(tree1.id) match {
        case Some(tree) => DrawPlayfield.setThreePosition(tree, tree1, myPos)
        case None       => treeGroup.add(createTree(tree1, myPos))
      }
    }
  }

  def createTree(tree: Tree, myPos: Vector3): Object3D = {
    val newTree: Object3D = CreateTree.randomTree()

    DrawPlayfield.setThreePosition(newTree, tree, myPos)

    newTree.name = tree.id.id.toString
    newTree
  }
}
