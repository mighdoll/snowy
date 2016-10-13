package snowy.server

import scala.collection.mutable
import snowy.GameClientProtocol._
import snowy.playfield._
import socketserve.ConnectionId
import GameSeeding.randomTrees


/** Records the current state of sleds, trees, snowballs etc. */
trait GameState {
  self: GameControl =>

  val gridSpacing = 100.0
  var sleds = Store[Sled]()
  var snowballs = Store[Snowball]()
  val trees: Set[Tree] = randomTrees()
  val users = mutable.Map[ConnectionId, User]()
  val sledMap = mutable.Map[ConnectionId, PlayId[Sled]]()
  var lastTime = System.currentTimeMillis()
  val commands = new PendingCommands

  /** Package the relevant state to communicate to the client */
  protected def currentState(): Iterable[(ConnectionId, State)] = {
    val clientSnowballs = snowballs.items.toSeq

    sleds.items.map { mySled =>
      val otherSleds = sleds.items.filter(_.id != mySled.id).toSeq
      mySled.connectionId -> State(mySled, otherSleds, clientSnowballs)
    }.toSeq
  }

  implicit class SledIdOps(id: PlayId[Sled]) {
    def sled: Sled = {
      sleds.items.find(_.id == id).get
    }

    def user: User = users(id.connectionId)

    def connectionId: ConnectionId = {
      sledMap.collectFirst {
        case (connectionId, sledId) if id == sledId =>
          connectionId
      }.get
    }
  }

  implicit class SledIndices(sled: Sled) {
    def connectionId: ConnectionId = {
      sledMap.collectFirst {
        case (connectionId, sledId) if sled.id == sledId =>
          connectionId
      }.get
    }

    def remove(): Unit = {
      sledMap.remove(sled.id.connectionId)
      sleds = sleds.remove(sled)
    }

  }

}
