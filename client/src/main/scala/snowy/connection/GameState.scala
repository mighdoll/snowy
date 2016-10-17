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

  //When the client receives the state of canvas, draw all sleds
  def receivedState(state: State): Unit = {
    clearScreen()
    serverSnowballs = Store(state.snowballs)
    serverSleds = Store(state.sleds)
    serverMySled = state.mySled

    drawState(serverSnowballs, serverSleds, serverMySled, serverTrees, gPlayField)
  }
}
