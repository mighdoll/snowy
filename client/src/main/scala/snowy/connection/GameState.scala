package snowy.connection

import snowy.GameClientProtocol._
import snowy.client.ClientDraw._
import snowy.playfield._

object GameState {
  var gPlayField = Playfield(0, 0) // A playfield dummy until the game receives a different one

  // TODO add a gametime timestamp to these, and organize together into a class
  var serverTrees = Store[Tree]()
  var serverSnowballs = Store[Snowball]()
  var serverSleds = Store[Sled]()
  var serverMySled = Sled.dummy

  /** @param items a seq of any plafield object
    * @return a store with grid an items given a seq of playfield objects */
  def createStore[A <: PlayfieldObject](items: Seq[A]): Store[A] = {
    Store[A](items = items.toSet, grid = Grid(items = items))
  }

  def updateStore[A <: PlayfieldObject](store: Store[A], items: Seq[A]): Store[A] = {
    items.foldLeft(store) { (newStore, item) =>
      newStore.insertById(item)
    }
  }

  //When the client receives the state of canvas, draw all sleds
  def receivedState(state: State): Unit = {
    clearScreen()
    serverSnowballs = createStore(state.snowballs)
    serverSleds = createStore(state.sleds)
    serverMySled = state.mySled

    drawState(serverSnowballs, serverSleds, serverMySled, serverTrees, gPlayField)
  }
}
