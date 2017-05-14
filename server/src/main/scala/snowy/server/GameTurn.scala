package snowy.server

import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging
import snowy.Awards._
import snowy.GameConstants._
import snowy.collision.{CollideThings, Death, DeathList, SledTree}
import snowy.playfield.PlayId.BallId
import snowy.playfield.{Sled, _}
import snowy.util.Span
import snowy.util.Span.{time, timeSpan}

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
  def turn(deltaSeconds: Double)(implicit snowballTracker: PlayfieldTracker[Snowball],
                                 sledTracker: PlayfieldTracker[Sled],
                                 span: Span): TurnDeaths =
    timeSpan("GameTurn.turn") { turnSpan =>
      gameHealth.recoverHealth(deltaSeconds)
      val expiredBalls = gameHealth.expireSnowballs()

      turnSpan.time("GameTurn.moveSnowballs") {
        state.motion.moveSnowballs(state.snowballs, deltaSeconds)
      }

      val moveAwards = turnSpan.time("GameTurn.moveSleds") {
        state.motion.moveSleds(state.sleds, deltaSeconds)
      }
      val collided = turnSpan.time("GameTurn.checkCollisions")(checkCollisions())
      val died     = gameHealth.collectDead()

      turnSpan.time("GameTurn.updateScore") {
        updateScore(moveAwards.toSeq ++ collided.killedSleds ++ died)
      }

      TurnDeaths(died, expiredBalls ++ collided.killedSnowballs)
    }

  case class CollisionResult(killedSleds: Traversable[SledKill],
                             killedSnowballs: Traversable[BallId])

  /** check for collisions between the sled and trees or snowballs */
  private def checkCollisions()(implicit snowballTracker: PlayfieldTracker[Snowball],
                                sledTracker: PlayfieldTracker[Sled]): CollisionResult = {
    import snowy.collision.GameCollide.snowballTrees

    // collide snowballs with sleds
    val sledSnowballDeaths: DeathList[Sled, Snowball] =
      CollideThings.collideCollectionWithGrid(
        state.sleds,
        state.snowballGrid
      )

    val deadBalls: Traversable[Snowball] =
      for { Death(snowball: Snowball, _) <- sledSnowballDeaths.b } yield { snowball }
    val uniqueDeadBalls = deadBalls.toSet
    uniqueDeadBalls.foreach(_.remove())

    // collide snowballs with trees
    val snowballTreeDeaths =
      for {
        snowball <- state.snowballs
        nearTrees = state.trees.grid.inside(snowball.boundingBox)
        if snowballTrees(snowball, nearTrees)
      } yield snowball

    for (ball <- snowballTreeDeaths) {
      ball.remove()
    }

    // collide snowballs with each other
    val snowballDeaths =
      CollideThings
        .collideCollection(state.snowballs, state.snowballGrid)
        .map(_.killed)
        .toSet

    for (snowball <- snowballDeaths) { snowball.remove() }

    // collide sleds with trees
    for {
      sled <- state.sleds
      nearTrees = state.trees.grid.inside(sled.boundingBox)
    } state.sledTree.collide(sled, nearTrees)

    // collide sleds with each other
    val sledDeaths = CollideThings.collideCollection(state.sleds, state.sledGrid)

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

      snowballDeaths.map(_.id) ++ bySled ++ snowballTreeDeaths.map(_.id)
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
            math.max(score * Points.sledLoss, Points.minPoints)
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
