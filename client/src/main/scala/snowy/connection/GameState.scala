package snowy.connection

import scala.collection.mutable
import snowy.GameClientProtocol._
import snowy.draw.{ThreeSleds, ThreeSnowballs}
import snowy.playfield.GameMotion._
import snowy.playfield.PlayId.{BallId, SledId}
import snowy.playfield._
import vector.Vec2d
import snowy.playfield.PlayfieldTracker

case class PlayfieldState(mySled: Sled,
                          sleds: Set[Sled],
                          snowballs: Set[Snowball],
                          trees: Set[Tree],
                          playfield: Vec2d)

object GameState {
  var gPlayField               = Vec2d(0, 0) // A playfield dummy until the game receives a different one
  var scoreboard               = Scoreboard(0, Seq())
  var mySledId: Option[SledId] = None
  implicit val nullSnowballTracker = PlayfieldTracker.nullSnowballTracker
  implicit val nullSledTracker = PlayfieldTracker.nullSledTracker

  // TODO add a gametime timestamp to these, and organize together into a class
  var serverTrees             = Set[Tree]()
  private var serverSnowballs = mutable.HashSet[Snowball]()
  private var serverSleds     = mutable.HashSet[Sled]()
  var gameTime                = 0L

  def serverMySled: Option[Sled] = {
    for {
      id   <- mySledId
      sled <- serverSleds.find(_.id == id)
    } yield sled
  }

  private var gameLoop: Option[Int] = None
  private var turning: Turning      = NoTurn
  var serverGameClock
    : Option[ServerGameClock] = None // HACK! TODO make GameState an instance

  /** set the client state to the state from the server
    * @param state the state sent from the server */
  def receivedState(state: State): Unit = {
    serverSnowballs = mutable.HashSet(state.snowballs: _*)
    serverSleds = mutable.HashSet(state.sleds: _*)
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
    moveSnowballs(serverSnowballs, deltaSeconds)
    serverMySled.foreach { mySled =>
      applyTurn(mySled, deltaSeconds)
      moveOneSled(mySled, deltaSeconds)
    }
    moveSleds(serverSleds, deltaSeconds)
    PlayfieldState(
      serverMySled.getOrElse(Sled.dummy),
      serverSleds.toSet,
      serverSnowballs.toSet,
      serverTrees,
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


  def removeSleds(removedIds: Seq[SledId]): Unit = {
    removeById[Sled](removedIds, serverSleds)
    ThreeSleds.removeSleds(removedIds)
  }

  def removeSnowballs(removedIds: Seq[BallId]): Unit = {
    removeById[Snowball](removedIds, serverSnowballs)
    ThreeSnowballs.removeSnowballs(removedIds)
  }

  /** remove a collection of sled or snowballs from from the store */
  private def removeById[A <: PlayfieldItem[A]](ids: Traversable[PlayId[A]],
                                                set: mutable.HashSet[A]): Unit = {
    val removedItems =
      for {
        itemId <-ids
        item <- set.find(_.id == itemId)
      } yield item

    removedItems.foreach(set.remove)
  }
}
