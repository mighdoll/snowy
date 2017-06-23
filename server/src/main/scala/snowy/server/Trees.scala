package snowy.server

import snowy.playfield.{Playfield, Tree}

class Trees(protected val playfield: Playfield) extends GridItems[Tree] {
  private val gameSeeding  = new TreeSeeding(playfield)
  private val initialTrees = gameSeeding.randomTrees()
  items ++= initialTrees
  initialTrees.foreach(grid.add)
}
