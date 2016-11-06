package snowy.connection

import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.client.ClientDraw._
import snowy.playfield._
import vector.Vec2d
import snowy.playfield.GameMotion._

object GameState {
  var gPlayField = Vec2d(0, 0) // A playfield dummy until the game receives a different one
  var scoreboard = Scoreboard(0, Seq())

  // TODO add a gametime timestamp to these, and organize together into a class
  var serverTrees     = Store[Tree]()
  var serverSnowballs = Store[Snowball]()
  var serverSleds     = Store[Sled]()
  var serverMySled    = Sled.dummy
  var gameTime        = 0L

  var gameLoop: Option[Int] = None

  var turning: Turning = NoTurn

  def startTurn(direction: Turning): Unit = {
    turning = direction
  }

  private def nextTimeSlice(): Double = {
    val currentTime  = System.currentTimeMillis()
    val deltaSeconds = (currentTime - gameTime) / 1000.0
    gameTime = currentTime
    deltaSeconds
  }

  //When the client receives the state of canvas, draw all sleds
  def receivedState(state: State): Unit = {
    serverSnowballs = Store(state.snowballs)
    serverSleds = Store(state.sleds)
    serverMySled = state.mySled
    gameTime = state.gameTime
  }

  def startRedraw(): Unit = {
    val loop = window.setInterval(() => {
      val deltaSeconds = nextTimeSlice()
      refresh(deltaSeconds)
    }, 20)
    gameLoop = Some(loop)
  }

  def stopRedraw(): Unit = gameLoop.foreach(id => window.clearInterval(id))

  def applyTurn(sled: Sled, deltaSeconds: Double): Sled = {
    turning match {
      case NoTurn    => sled
      case RightTurn => GameMotion.turnSled(serverMySled, RightTurn, deltaSeconds)
      case LeftTurn  => GameMotion.turnSled(serverMySled, LeftTurn, deltaSeconds)
    }
  }

  private def moveOneSled(sled: Sled, deltaSeconds: Double): Sled = {
    val (store, _) = moveSleds(Store(Seq(sled)), deltaSeconds)
    store.items.head
  }

  def nextState(deltaSeconds: Double): Unit = {
    serverSnowballs = moveSnowballs(serverSnowballs, deltaSeconds)
    val turnedSled    = applyTurn(serverMySled, deltaSeconds)
    val movedSled     = moveOneSled(turnedSled, deltaSeconds)
    val (newSleds, _) = moveSleds(serverSleds, deltaSeconds)
    serverSleds = newSleds
    serverMySled = movedSled
  }

  def refresh(deltaSeconds: Double): Unit = {
    nextState(deltaSeconds)

    clearScreen()
    drawState(
      serverSnowballs,
      serverSleds,
      serverMySled,
      serverTrees,
      gPlayField,
      scoreboard)
  }

}
