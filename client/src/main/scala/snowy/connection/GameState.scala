package snowy.connection

import snowy.GameClientProtocol._
import snowy.playfield.GameMotion._
import snowy.playfield.PlayId.SledId
import snowy.playfield._
import vector.Vec2d

case class PlayfieldState(mySled: Sled,
                          sleds: Set[Sled],
                          snowballs: Set[Snowball],
                          trees: Set[Tree],
                          playfield: Vec2d)

object GameState {
  var gPlayField               = Vec2d(0, 0) // A playfield dummy until the game receives a different one
  var scoreboard               = Scoreboard(0, Seq())
  var mySledId: Option[SledId] = None

  // TODO add a gametime timestamp to these, and organize together into a class
  var serverTrees             = Store[Tree]()
  private var serverSnowballs = Store[Snowball]()
  private var serverSleds     = Store[Sled]()
  var gameTime                = 0L

  // TODO: return Option[Sled]. The server might send state before we join and have a sled
  def serverMySled: Option[Sled] = {
    for {
      id   <- mySledId
      sled <- serverSleds.items.find(_.id == id)
    } yield sled
  }

  private var gameLoop: Option[Int] = None
  private var turning: Turning      = NoTurn
  var serverGameClock
    : Option[ServerGameClock] = None // HACK! TODO make GameState an instance

  /** set the client state to the state from the server
    * @param state the state sent from the server */
  def receivedState(state: State): Unit = {
    serverSnowballs = Store(state.snowballs)
    serverSleds = Store(state.sleds)
    gameTime = state.gameTime
  }

  def startTurn(direction: Turning): Unit = turning = direction

  private def applyTurn(sled: Sled, deltaSeconds: Double): Unit = {
    turning match {
      case RightTurn => GameMotion.turnSled(sled, RightTurn, deltaSeconds)
      case LeftTurn  => GameMotion.turnSled(sled, LeftTurn, deltaSeconds)
      case NoTurn    => None
    }
  }

  private def moveOneSled(sled: Sled, deltaSeconds: Double): Unit = {
    moveSleds(List(sled), deltaSeconds)
  }

  // TODO Use the same turns that the server does
  def nextState(deltaSeconds: Double): PlayfieldState = {
    moveSnowballs(serverSnowballs.items, deltaSeconds)
    serverMySled.foreach { mySled =>
      applyTurn(mySled, deltaSeconds)
      moveOneSled(mySled, deltaSeconds)
    }
    moveSleds(serverSleds.items, deltaSeconds)
    PlayfieldState(
      serverMySled.getOrElse(Sled.dummy),
      serverSleds.items,
      serverSnowballs.items,
      serverTrees.items,
      gPlayField
    )
  }

  /** Advance to the next game simulation frame.
    *
    * @return the time in seconds since the last frame */
  def nextTimeSlice(): Double = {
    val newTurn =
      serverGameClock.map(_.serverGameTime).getOrElse(System.currentTimeMillis())
    val deltaSeconds = (newTurn - gameTime) / 1000.0
    gameTime = newTurn
    deltaSeconds
  }
}
