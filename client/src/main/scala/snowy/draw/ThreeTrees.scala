package snowy.draw

import minithree.THREE.Object3D
import minithree.raw.Vector3
import snowy.client.DrawState.Groups
import snowy.client.{CreateTree, DrawState, UpdateGroup}
import snowy.playfield.Tree

object ThreeTrees {
  val treeGroup = new UpdateGroup[Tree](Groups.threeTrees)
  def updateThreeTrees(trees: Set[Tree], myPos: Vector3): Unit = {
    trees.foreach { tree1 =>
      treeGroup.map.get(tree1.id) match {
        case Some(tree) => DrawState.setThreePosition(tree, tree1, myPos)
        case None       => treeGroup.add(createTree(tree1, myPos))
      }
    }
  }

  def createTree(tree: Tree, myPos: Vector3): Object3D = {
    val newTree: Object3D = CreateTree.randomTree()

    DrawState.setThreePosition(newTree, tree, myPos)

    newTree.name = tree.id.id.toString
    newTree
  }
}
