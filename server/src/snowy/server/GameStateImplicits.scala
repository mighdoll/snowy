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

    def serverSled: Option[ServerSled] = {
      state.sledMap.valuesIterator.collectFirst {
        case serverSled: ServerSled if serverSled.sled.id == id => serverSled
      }
    }

    def user: Option[User] = id.connectionId.map(state.users(_))

    def connectionId: Option[ClientId] = {
      state.sledMap.collectFirst {
        case (connectionId, serverSled) if id == serverSled.id =>
          connectionId
      }
    }
  }

  implicit class ConnectionIdOps(id: ClientId) {
    def sled: Option[Sled] = state.sledMap.get(id).map(_.sled)
  }

  implicit class SnowballOps(val snowball: Snowball) {
    def remove(): Unit = {
      state.snowballs.remove(snowball)
    }
  }

  implicit class SledOps(sled: Sled) {
    def connectionId: Option[ClientId] = {
      state.sledMap.collectFirst {
        case (connectionId, serverSled) if sled.id == serverSled.id =>
          connectionId
      }
    }

    def remove(): Unit = {
      sled.id.connectionId.foreach(state.sledMap.remove(_))
      state.sleds.remove(sled)
    }
  }

  implicit class UserOps(user: User) {
    def sled: Option[ServerSled] = {
      val optClientId =
        state.users.collectFirst {
          case (clientId: ClientId, theUser: User) if user == theUser => clientId
        }
      for {
        clientId <- optClientId
        sled     <- state.sledMap.get(clientId)
      } yield {
        sled
      }
    }
  }

  implicit def snowballGrid = state.snowballs.grid
  implicit def treeGrid     = state.trees.grid
  implicit def sledGrid     = state.sleds.grid
}
