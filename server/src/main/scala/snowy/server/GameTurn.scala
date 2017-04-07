package snowy.server

import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging
import snowy.Awards._
import snowy.GameConstants._
import snowy.collision.{CollideThings, Death, DeathList, SledTree}
import snowy.playfield.GameMotion._
import snowy.playfield.PlayId.BallId
import snowy.playfield.{Sled, _}
import snowy.util.Span
import snowy.util.Span.time

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

  case class TurnDeaths(deadSleds: Traversable[SledDied],
                        deadSnowBalls: Traversable[BallId])

  /** Called to update game state on a regular timer */
  def turn(deltaSeconds: Double)(implicit span:Span): TurnDeaths = time("GameTurn.turn") {
    gameHealth.recoverHealth(deltaSeconds)
    val expiredBalls = gameHealth.expireSnowballs()

    moveSnowballs(state.snowballs.items, deltaSeconds)

    val moveAwards = moveSleds(state.sleds.items, deltaSeconds)
    val collided   = time("checkCollisions")(checkCollisions())
    val died       = gameHealth.collectDead()

    updateScore(moveAwards.toSeq ++ collided.killedSleds ++ died)
    TurnDeaths(died, expiredBalls ++ collided.killedSnowballs)
  }

  case class CollisionResult(killedSleds: Traversable[SledKill],
                             killedSnowballs: Traversable[BallId])

  /** check for collisions between the sled and trees or snowballs */
  private def checkCollisions(): CollisionResult = {
    import snowy.collision.GameCollide.snowballTrees

    // collide snowballs with sleds
    val sledSnowballDeaths: DeathList[Sled, Snowball] =
      CollideThings.collideThings(state.sleds.items, state.snowballs.items)

    for (Death(killed: Snowball, killer: Sled) <- sledSnowballDeaths.b) {
      state.snowballs = state.snowballs.removeMatchingItems(_.id == killed.id)
    }

    // collide snowballs with trees
    val snowballTreeDeaths = state.snowballs.items.filter(snowballTrees(_, state.trees))
    for (ball <- snowballTreeDeaths)
      state.snowballs = state.snowballs.remove(ball)

    // collide snowballs with each other
    val snowballDeaths = CollideThings.collideCollection(state.snowballs.items)

    for (Death(killed: Snowball, _) <- snowballDeaths) {
      state.snowballs = state.snowballs.removeMatchingItems(_.id == killed.id)
    }

    // collide sleds with trees
    state.sleds.items.foreach(SledTree.collide(_, state.trees))

    // collide sleds with each other
    val sledDeaths = CollideThings.collideCollection(state.sleds.items)

    // accumulate awards
    val snowballAwards =
      for (Death(killed: Sled, killer: Snowball) <- sledSnowballDeaths.a)
        yield SledKill(killer.ownerId, killed.id)

    val sledAwards = sledDeaths.map {
      case Death(killed: Sled, killer: Sled) => SledKill(killer.id, killed.id)
    }

    val deadSnowballs = {
      val bySled =
        for (Death(killed: Snowball, killer: Sled) <- sledSnowballDeaths.b)
          yield killed.id

      snowballDeaths.map(_.killed.id) ++ bySled ++ snowballTreeDeaths.map(_.id)
    }

    CollisionResult(snowballAwards ++ sledAwards, deadSnowballs)
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
          val points = loser.score * Points.sledKill
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
            Math.max(score * Points.sledLoss, Points.minPoints)
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
    val deltaMicros     = (deltaSeconds * secondsToMicros).toLong
//    Perf.record("deltaSeconds", deltaMicros)
  }

}
