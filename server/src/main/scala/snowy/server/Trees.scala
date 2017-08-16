package snowy.server

import com.typesafe.config.Config
import snowy.playfield.{Playfield, Tree}

class Trees(protected val playfield: Playfield, snowyConfig:Config) extends GridItems[Tree] {
  private val gameSeeding  = new TreeSeeding(playfield)

  private val initialTrees =
    if (snowyConfig.getBoolean("seed-trees")) gameSeeding.randomTrees()
    else Seq()

  items ++= initialTrees
  initialTrees.foreach(grid.add)
}
