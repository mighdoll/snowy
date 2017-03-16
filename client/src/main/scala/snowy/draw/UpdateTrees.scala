package snowy.draw

import minithree.THREE.Object3D
import minithree.raw.Vector3
import snowy.GameConstants
import snowy.client.DrawState2.{Bodies, Groups}
import snowy.client.{DrawState2, ThreeTree}
import snowy.playfield.Tree

object UpdateTrees {
  def updateCtrees(trees: Set[Tree]): Unit = {
    trees.foreach { tree1 =>
      var idExists = false
      Groups.ctrees.children.zipWithIndex.foreach {
        case (possibleTree, index) =>
          if (possibleTree.name == tree1.id.id.toString) {
            idExists = true
            val ctree = Groups.ctrees.children(index)
            val newPos = DrawState2.transformPositionMod(
              new Vector3(tree1.pos.x, 0, tree1.pos.y),
              Bodies.sled.position,
              new Vector3(GameConstants.playfield.x, 0, GameConstants.playfield.y)
            )
            ctree.position.x = newPos.x
            ctree.position.z = newPos.z
          }
      }
      if (!idExists) {
        addTree(tree1)
      }
    }
  }
  def addTree(tree: Tree): Unit = {
    val newTree: Object3D = ThreeTree.randomTree()
    val newPos = DrawState2.transformPositionMod(
      new Vector3(tree.pos.x, 0, tree.pos.y),
      Bodies.sled.position,
      new Vector3(GameConstants.playfield.x, 0, GameConstants.playfield.y)
    )
    newTree.position.x = newPos.x
    newTree.position.z = newPos.z
    newTree.name = tree.id.id.toString
    Groups.ctrees.add(newTree)
  }
}
