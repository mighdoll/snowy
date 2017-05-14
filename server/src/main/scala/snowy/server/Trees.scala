package snowy.server

import snowy.playfield.{Playfield, PowerUp, Tree}

class Trees (protected val playfield: Playfield) extends GridItems[Tree] {
  val gameSeeding           = new TreeSeeding(playfield)

  items ++= gameSeeding.randomTrees()
}
