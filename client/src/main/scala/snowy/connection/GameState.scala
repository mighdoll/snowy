package snowy.connection

import snowy.GameClientProtocol._
import snowy.GameConstants
import snowy.client.{Animation, ClientMain, DrawPlayfield}
import snowy.playfield.GameMotion._
import snowy.playfield.PlayId.{BallId, PowerUpId, SledId}
import snowy.playfield._
import vector.Vec2d

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

case class PlayfieldState(mySled: Sled,
                          sleds: Set[Sled],
                          snowballs: Set[Snowball],
                          trees: Set[Tree],
                          playfield: Vec2d)

class GameState(drawPlayfield: DrawPlayfield) {
  var gPlayField               = Vec2d(0, 0) // A playfield dummy until the game receives a different one
  var scoreboard               = Scoreboard(0, Seq())
  var mySledId: Option[SledId] = None
  import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers._

  val playfieldAnimation = new Animation(animate)
  val playfield          = new Playfield(GameConstants.oldPlayfieldSize)
  val motion             = new GameMotion(playfield)

  /** Update the client's playfield objects and draw the new playfield to the screen */
  private def animate(timestamp: Double): Unit = {
    val deltaSeconds = nextTimeSlice()
    val newState     = nextState(math.max(deltaSeconds, 0))

    drawPlayfield.drawPlayfield(
      newState.snowballs,
      newState.sleds,
      newState.mySled,
      newState.trees,
      newState.playfield
    )
  }

  def startRedraw(): Unit = playfieldAnimation.start()

  def stopRedraw(): Unit = playfieldAnimation.cancel()

  // TODO add a gametime timestamp to these, and organize together into a class
  var serverTrees             = Set[Tree]()
  private var serverSleds     = mutable.HashSet[Sled]()
  private var serverSnowballs = mutable.HashSet[Snowball]()
  private val serverPowerUps  = new mutable.HashSet[PowerUp]()
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
      case RightTurn => motion.turnSled(sled, RightTurn, deltaSeconds)
      case LeftTurn  => motion.turnSled(sled, LeftTurn, deltaSeconds)
      case NoTurn    => None
    }
  }

  private def moveOneSled(sled: Sled, deltaSeconds: Double): Unit = {
    motion.moveSleds(List(sled), deltaSeconds)
  }

  // TODO Use the same turns that the server does
  def nextState(deltaSeconds: Double): PlayfieldState = {
    motion.moveSnowballs(serverSnowballs, deltaSeconds)
    serverMySled.foreach { mySled =>
      applyTurn(mySled, deltaSeconds)
      moveOneSled(mySled, deltaSeconds)
    }
    motion.moveSleds(serverSleds, deltaSeconds)
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
    ClientMain.loadedGeometry.threeGroupsFuture
      .foreach(_.threeSleds.removeSleds(removedIds))
  }

  def removeSnowballs(removedIds: Seq[BallId]): Unit = {
    removeById[Snowball](removedIds, serverSnowballs)
    ClientMain.loadedGeometry.threeGroupsFuture
      .foreach(_.threeSnowballs.removeSnowballs(removedIds))
  }

  def removePowerUps(removedIds: Seq[PowerUpId]): Unit = {
    removeById[PowerUp](removedIds, serverPowerUps)
    ClientMain.loadedGeometry.threeGroupsFuture
      .foreach(_.threePowerups.removePowerup(removedIds))
  }

  def addPlayfieldItems(items: Seq[SharedItem]): Unit = {
    val newUps       = items.collect { case powerUp: PowerUp   => powerUp }
    val newSleds     = items.collect { case sled: Sled         => sled }
    val newSnowballs = items.collect { case snowball: Snowball => snowball }

    serverPowerUps ++= newUps
    serverSleds ++= newSleds
    serverSnowballs ++= newSnowballs

    ClientMain.loadedGeometry.threeGroupsFuture.foreach { groups =>
      newUps.foreach(groups.threePowerups.addPowerup)
      newSleds.foreach(groups.threeSleds.addSled(_, false))
      newSnowballs.foreach(groups.threeSnowballs.addSnowball)
    }
  }

  def sledNameFromId(sledId: SledId): Option[String] =
    serverSleds.find(_.id == sledId).map(_.userName)

  /** remove a collection of sled or snowballs from from the store */
  private def removeById[A <: PlayfieldItem[A]](ids: Traversable[PlayId[A]],
                                                set: mutable.HashSet[A]): Unit = {
    val removedItems =
      for {
        itemId <- ids
        item   <- set.find(_.id == itemId)
      } yield item

    removedItems.foreach(set.remove)
  }
}
