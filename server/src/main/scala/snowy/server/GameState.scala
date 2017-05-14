package snowy.server

import scala.collection.mutable
import snowy.GameClientProtocol._
import snowy.GameConstants
import snowy.collision.SledTree
import snowy.playfield.PlayId.SledId
import snowy.playfield._
import socketserve.ClientId
import vector.Vec2d

/** Records the current state of sleds, trees, snowballs etc. */
trait GameState { self: GameControl =>

  val playfield = {
    val playfieldConfig = GlobalConfig.snowyConfig.getConfig("playfield")
    val width           = playfieldConfig.getInt("width")
    val height          = playfieldConfig.getInt("height")
    new Playfield(Vec2d(width, height))
  }
  private val gridSpacing   = 100.0
  implicit val sledGrid     = new Grid[Sled](playfield.size, gridSpacing)
  val users                 = mutable.Map[ClientId, User]()
  val sledMap               = mutable.Map[ClientId, SledId]()
  val pendingControls       = new PendingControls
  val gameStateImplicits    = new GameStateImplicits(this)
  val sleds                 = mutable.HashSet[Sled]()
  val snowballs             = new Snowballs(playfield)
  val trees                 = new Trees(playfield)
  val powerUps              = new PowerUps(playfield)
  val motion                = new GameMotion(playfield)
  val sledTree              = new SledTree(playfield)

  /** Package the relevant state to communicate to the client */
  protected def currentState(): State = {
    State(gameTurns.gameTime, sleds.toSeq, snowballs.items.toSeq)
  }

  def debugVerifyGridState(): Unit = {
    val sledSet     = sleds.toSet
    val sledGridSet = sledGrid.items
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
}
