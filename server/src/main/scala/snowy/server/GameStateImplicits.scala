package snowy.server

import snowy.playfield.PlayId.SledId
import snowy.playfield._
import socketserve.ClientId

/** Convenient ways to look up objects and users in the game state collections */
class GameStateImplicits(state: GameState) {

  implicit class SledIdOps(id: SledId) {
    def sled: Option[Sled] = {
      state.sleds.find(_.id == id)
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

  implicit class SnowballOps(val snowball: Snowball) {
    def remove(): Unit = {
      state.snowballs.grid.remove(snowball)
      state.snowballs.items.remove(snowball)
    }
  }

  implicit class SledOps(sled: Sled) {
    def connectionId: Option[ClientId] = {
      state.sledMap.collectFirst {
        case (connectionId, sledId) if sled.id == sledId =>
          connectionId
      }
    }

    def remove(): Unit = {
      sled.id.connectionId.foreach(state.sledMap.remove(_))
      state.sledGrid.remove(sled)
      state.sleds.remove(sled)
    }
  }

  implicit def snowballGrid = state.snowballs.grid
  implicit def treeGrid = state.trees.grid
  implicit def sledGrid = state.sledGrid
}
