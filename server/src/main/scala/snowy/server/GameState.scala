package snowy.server

import scala.collection.mutable
import snowy.GameClientProtocol._
import snowy.playfield.PlayId.SledId
import snowy.playfield._
import snowy.server.GameSeeding.randomTrees
import socketserve.ConnectionId

/** Records the current state of sleds, trees, snowballs etc. */
trait GameState { self: GameControl =>

  val gridSpacing        = 100.0
  val trees: Set[Tree]   = randomTrees()
  val users              = mutable.Map[ConnectionId, User]()
  val sledMap            = mutable.Map[ConnectionId, SledId]()
  val commands           = new PendingCommands
  val gameStateImplicits = new GameStateImplicits(this)
  var sleds              = Store[Sled]()
  var snowballs          = Store[Snowball]()
  import gameStateImplicits._

  /** Package the relevant state to communicate to the client */
  protected def currentState(): Iterable[(ConnectionId, State)] = {
    val clientSnowballs = snowballs.items.toSeq

    (for {
      mySled       <- sleds.items
      connectionId <- mySled.connectionId
    } yield {
      val otherSleds = sleds.items.filter(_.id != mySled.id).toSeq
      connectionId -> State(gameTurns.gameTime, mySled, otherSleds, clientSnowballs)
    }).toSeq
  }

}
