package snowy.server

import scala.collection.mutable
import snowy.GameClientProtocol._
import snowy.playfield.PlayId.SledId
import snowy.playfield._
import snowy.server.GameSeeding.randomTrees
import socketserve.ClientId

/** Records the current state of sleds, trees, snowballs etc. */
trait GameState { self: GameControl =>

  val gridSpacing        = 100.0
  val trees: Set[Tree]   = randomTrees()
  val users              = mutable.Map[ClientId, User]()
  val sledMap            = mutable.Map[ClientId, SledId]()
  val pendingControls    = new PendingControls
  val gameStateImplicits = new GameStateImplicits(this)
  var sleds              = Store[Sled]()
  var snowballs          = Store[Snowball]()

  /** Package the relevant state to communicate to the client */
  protected def currentState(): State = {
    State(gameTurns.gameTime, sleds.items.toSeq, snowballs.items.toSeq)
  }

}
