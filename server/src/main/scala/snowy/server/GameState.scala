package snowy.server

import scala.collection.mutable
import snowy.GameClientProtocol._
import snowy.GameConstants
import snowy.playfield.PlayId.SledId
import snowy.playfield._
import snowy.server.GameSeeding.randomTrees
import socketserve.ClientId

/** Records the current state of sleds, trees, snowballs etc. */
trait GameState { self: GameControl =>

  val gridSpacing           = 100.0
  implicit val sledGrid     = new Grid[Sled](GameConstants.playfield, gridSpacing)
  implicit val treeGrid     = new Grid[Tree](GameConstants.playfield, gridSpacing)
  implicit val snowballGrid = new Grid[Snowball](GameConstants.playfield, gridSpacing)
  val users                 = mutable.Map[ClientId, User]()
  val sledMap               = mutable.Map[ClientId, SledId]()
  val pendingControls       = new PendingControls
  val gameStateImplicits    = new GameStateImplicits(this)
  val trees: Set[Tree]      = randomTrees()
  val sleds                 = mutable.HashSet[Sled]()
  val snowballs             = mutable.HashSet[Snowball]()

  /** Package the relevant state to communicate to the client */
  protected def currentState(): State = {
    State(gameTurns.gameTime, sleds.toSeq, snowballs.toSeq)
  }

  def debugVerifyGridState():Unit = {
    val sledSet = sleds.toSet
    val sledGridSet = sledGrid.items
    if (sledSet != sledGridSet) {
      logger.error("sledSet != sledGridSet")
      logger.error(s"sledSet:     $sledSet")
      logger.error(s"sledGridSet: $sledGridSet")
    }
    val snowballSet = snowballs.toSet
    val snowballGridSet = snowballGrid.items
    if (snowballSet != snowballGridSet) {
      logger.error("snowballSet != snowballGridSet")
      logger.error(s"snowballSet:     $snowballSet")
      logger.error(s"snowballGridSet: $snowballGridSet")
    }
  }
}
