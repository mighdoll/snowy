package snowy.server

//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.GameClientProtocol._
import snowy.collision.SledTree
import snowy.playfield.PlayId.SledId
import snowy.playfield._
import socketserve.ClientId
import vector.Vec2d

import scala.collection.mutable

/** Records the current state of sleds, trees, snowballs etc. */
trait GameState extends Logging { self: GameControl =>

  val playfield = {
    val playfieldConfig = GlobalConfig.snowy.getConfig("playfield")
    val width           = playfieldConfig.getInt("width")
    val height          = playfieldConfig.getInt("height")
    new Playfield(Vec2d(width, height))
  }
  val users       = mutable.Map[ClientId, User]()
  val sledMap     = mutable.Map[ClientId, ServerSled]()
  def serverSleds = sledMap.values

  val sleds     = new Sleds(playfield)
  val snowballs = new Snowballs(playfield)
  val trees     = new Trees(playfield, self.snowyConfig)
  val powerUps  = new PowerUps(playfield)

  val motion   = new GameMotion(playfield)
  val sledTree = new SledTree(playfield)

  val gameStateImplicits = new GameStateImplicits(this)

  debugVerifyTreeState()

  /** Package the relevant state to communicate to the client */
  protected def currentState(): State = {
    State(playfieldSteps.gameTime, sleds.items.toSeq, snowballs.items.toSeq)
  }

  def debugVerifyGridState(): Unit = {
    val sledSet     = sleds.items.toSet
    val sledGridSet = sleds.grid.items
    if (sledSet != sledGridSet) {
      logger.error("sledSet != sledGridSet")
      logger.error(s"sledSet:     $sledSet")
      logger.error(s"sledGridSet: $sledGridSet")
    }
    val snowballSet     = snowballs.items.toSet
    val snowballGridSet = snowballs.grid.items
    if (snowballSet != snowballGridSet) {
      logger.error("snowballSet != snowballGridSet")
      logger.error(s"snowballSet:     $snowballSet")
      logger.error(s"snowballGridSet: $snowballGridSet")
    }
  }

  def debugVerifyTreeState(): Unit = {
    val treeSet     = trees.items.toSet
    val treeGridSet = trees.grid.items
    if (treeSet != treeGridSet) {
      logger.error("treeSet != treeGridSet")
      logger.error(s"treeSet:     $treeSet")
      logger.error(s"treeGridSet: $treeGridSet")
    }
  }

}
