package snowy.server

import snowy.playfield.PlayId.SledId
import snowy.playfield._
import socketserve.ClientId

/** Convenient ways to look up objects and users in the game state collections */
class GameStateImplicits(state: GameState) {

  implicit class SledIdOps(id: SledId) {
    def sled: Option[Sled] = {
      state.sleds.items.find(_.id == id)
    }

    def user: Option[User] = id.connectionId.map(state.users(_))

    def connectionId: Option[ClientId] = {
      state.sledMap.collectFirst {
        case (connectionId, sledId) if id == sledId =>
          connectionId
      }
    }
  }

  implicit class ConnectionIdOps(id: ClientId) {
    def sled: Option[Sled] = state.sledMap.get(id).flatMap(_.sled)
  }

  implicit class SledIndices(sled: Sled) {
    def connectionId: Option[ClientId] = {
      state.sledMap.collectFirst {
        case (connectionId, sledId) if sled.id == sledId =>
          connectionId
      }
    }

    def remove(): Unit = {
      sled.id.connectionId.foreach(state.sledMap.remove(_))
      state.sleds = state.sleds.remove(sled)
    }
  }
}
