package snowy.server

import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging
import snowy.Awards._
import snowy.GameConstants._
import snowy.collision.{CollideThings, Death, SledTree}
import snowy.playfield.GameMotion._
import snowy.playfield.{Sled, _}
import snowy.util.Perf
import snowy.util.Perf.time

class GameTurn(state: GameState, tickDelta: FiniteDuration) extends StrictLogging {
  val gameHealth         = new GameHealth(state)
  val gameStateImplicits = new GameStateImplicits(state)
  var gameTime           = System.currentTimeMillis()
  var lastGameTime       = gameTime - tickDelta.toMillis
  import gameStateImplicits._

  /** advance to the next game time
    * @return seconds since the last turn
    */
  def nextTurn(): Double = {
    val deltaSeconds = nextTimeSlice()
    recordTurnJitter(deltaSeconds)
    deltaSeconds
  }

  /** Called to update game state on a regular timer */
  def turn(deltaSeconds: Double): Traversable[SledDied] = time("gameTurn") {
//    Thread.sleep(18)  // for simulating a slow server
    gameHealth.recoverHealth(deltaSeconds)
    gameHealth.recoverPushEnergy(deltaSeconds)
    gameHealth.expireSnowballs()

    moveSnowballs(state.snowballs.items, deltaSeconds)

    val moveAwards      = time("moveSleds")(moveSleds(state.sleds.items, deltaSeconds))
    val collisionAwards = time("checkCollisions")(checkCollisions())
    val died            = gameHealth.collectDead()

    updateScore(moveAwards.toSeq ++ collisionAwards ++ died)
    died
  }

  /** check for collisions between the sled and trees or snowballs */
  private def checkCollisions(): Traversable[SledKill] = {
    import snowy.collision.GameCollide.snowballTrees

    // collide snowballs with sleds
    val snowballDeaths =
      CollideThings.collideThings(state.sleds.items, state.snowballs.items)

    val snowballAwards = snowballDeaths.flatMap {
      case Death(killed: Sled, killer: Snowball) =>
        Some(SledKill(killer.ownerId, killed.id))
      case Death(killed: Snowball, killer: Sled) =>
        state.snowballs = state.snowballs.removeMatchingItems(_.id == killed.id)
        None
    }

    val snowballSnowballDeaths = CollideThings.collideCollection(state.snowballs.items)
    snowballSnowballDeaths.foreach {
      case Death(killed: Snowball, _) =>
        state.snowballs = state.snowballs.removeMatchingItems(_.id == killed.id)
    }

    state.sleds.items.foreach(SledTree.collide(_, state.trees))

    val sledDeaths = CollideThings.collideCollection(state.sleds.items)
    val sledAwards = sledDeaths.map {
      case Death(killed: Sled, killer: Sled) => SledKill(killer.id, killed.id)
    }

    state.snowballs = state.snowballs.removeMatchingItems(snowballTrees(_, state.trees))

    snowballAwards ++ sledAwards
  }

  /** update the score based on sled travel distance, sleds killed, etc. */
  private def updateScore(awards: Seq[Award]): Unit = {
    awards.foreach {
      case SledKill(winnerId, loserId) =>
        for {
          winnerConnectionId <- winnerId.connectionId
          winner             <- winnerId.user
          loser              <- loserId.user
        } {
          val points = loser.score / 2
          winner.addScore(points)
        }
      case Travel(sledId, distance) =>
        for {
          connectionId <- sledId.connectionId
          user         <- sledId.user
        } {
          val points = distance * Points.travel
          user.addScore(points)
        }
      case SnowballHit(winnerId) =>
      case SledDied(loserId) =>
        for {
          connectionId <- loserId.connectionId
          user         <- loserId.user
        } {
          user.setScore((score: Double) => {
            Math.max(score / 2, 10)
          })
        }
    }
  }

  /** Advance to the next game simulation state
    *
    * @return the time since the last time slice, in seconds
    */
  private def nextTimeSlice(): Double = {
    val currentTime  = System.currentTimeMillis()
    val deltaSeconds = (currentTime - gameTime) / 1000.0
    lastGameTime = gameTime
    gameTime = currentTime
    deltaSeconds
  }

  private def recordTurnJitter(deltaSeconds: Double): Unit = {
    val secondsToMicros = 1000000
    val offset          = 10 * secondsToMicros // library can't handle negative
    Perf.record(
      "turnJitter",
      offset + (deltaSeconds * secondsToMicros).toLong - tickDelta.toMicros
    )
  }

}
