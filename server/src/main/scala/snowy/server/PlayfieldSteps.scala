package snowy.server

import scala.concurrent.duration._
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.GameConstants._
import snowy.collision._
import snowy.measures.Span.{time, timeSpan}
import snowy.measures.{Gauged, Span}
import snowy.playfield.PlayId.{BallId, PowerUpId}
import snowy.playfield.{Sled, _}
import snowy.server.PlayfieldSteps._
import snowy.server.rewards.Achievements._
import snowy.server.rewards.PowerUpRewards
import snowy.util.ActorTypes.ParentSpan
import snowy.util.RemoveList.RemoveListOps

/** Support for moving the playfield objects to the next game state */
class PlayfieldSteps(state: GameState, tickDelta: FiniteDuration, clock: Clock)
    extends Logging {
  var gameTime           = clock.currentMillis
  var lastGameTime       = gameTime - tickDelta.toMillis
  val gameHealth         = new GameHealth(state)
  val gameStateImplicits = new GameStateImplicits(state)
  var currentKing        = state.serverSleds.headOption
  import gameStateImplicits._

  /** advance to the next game time
    * @return seconds since the last turn
    */
  def nextStep()(implicit parentSpan: Span): Double = {
    val deltaSeconds = nextTimeSlice()
    recordTurnJitter(deltaSeconds)
    deltaSeconds
  }

  /** Advance the playfield objects to the next simulation state.
    *
    * Moving objects move and collide with with each other. Sleds collect
    * powerups and other achievements.
    *
    * @return results of the playfield turn: sleds iced, snowballs removed, powerups collected, etc. */
  def step(deltaSeconds: Double)(implicit parentSpan: Span): TurnResults =
    timeSpan("Playfield.step") { implicit turnSpan =>
      gameHealth.recoverHealth(deltaSeconds)
      val expiredBalls = gameHealth.expireSnowballs(gameTime)
      time("moveSnowballs") {
        state.motion.moveSnowballs(state.snowballs.items, deltaSeconds)
      }

      time("moveSleds") {
        state.motion.moveSleds(state.sleds.items, deltaSeconds, gameTime)
      }

      val (usedPowerUpIds, powerUpAchievements) =
        collidePowerUps(state.sleds, state.powerUps)
      val collided     = checkCollisions()
      val achievements = trackIcings(collided.icings) ++ trackKing() ++ powerUpAchievements

      val died = gameHealth.collectDead()
      applyAchievements(achievements ++ collided.icings ++ died)

      val newPowerUps = state.powerUps.refresh(gameTime)

      TurnResults(
        died,
        expiredBalls ++ collided.killedSnowballs,
        usedPowerUpIds,
        newPowerUps,
        collided.icings,
        achievements
      )
    }

  /** Report an achievement if there's a new king */
  private def trackKing(): Option[Achievement] = {
    var max: Double = {
      val kingScore =
        for {
          king <- currentKing
          if state.sleds.items.contains(king.sled) // sled has not died
        } yield {
          king.user.score
        }
      kingScore.getOrElse(0)
    }
    val oldKing = currentKing
    val newCandidates =
      for {
        serverSled <- state.serverSleds
        if serverSled.user.score > max
        if Some(serverSled) != currentKing
      } yield {
        max = serverSled.user.score
        serverSled
      }
    newCandidates.lastOption.map { newKing =>
      currentKing = Some(newKing)
      Kinged(newKing, oldKing)
    }
  }

  case class CollisionResult(icings: Traversable[SledIced],
                             killedSnowballs: Traversable[BallId])

  /** For any sleds that hit a power up,
    * @return the power up and an achievement for the sled.
    */
  private def collidePowerUps[_: ParentSpan](
        sleds: Sleds,
        powerUps: PowerUps
  ): (Iterable[PowerUpId], Iterable[PowerUpCollected]) =
    time("collidePowerUps") {
      val winners =
        for {
          powerUp <- powerUps.items
          sled    <- sleds.grid.inside(powerUp.boundingBox)
          if Collisions.circularCollide(powerUp, sled)
        } yield {
          (powerUp, sled)
        }

      val results =
        for {
          (powerUp, sled) <- winners
          reward = PowerUpRewards.reward(powerUp)
          serverSled <- sled.id.serverSled
        } yield {
          powerUps.removePowerUp(powerUp, gameTime)
          (powerUp.id, PowerUpCollected(serverSled, reward))
        }

      results.unzip
    }

  /** Check for collisions between the sled and trees or snowballs */
  private def checkCollisions()(implicit snowballTracker: PlayfieldTracker[Snowball],
                                sledTracker: PlayfieldTracker[Sled],
                                parentSpan: Span): CollisionResult =
    time("checkCollisions") {
      import snowy.collision.GameCollide.snowballTrees
      // collide snowballs with sleds
      val sledSnowballDeaths: DeathList[Sled, Snowball] =
        CollideThings.collideWithGrid(
          state.sleds.items,
          state.snowballs.grid
        )

      val deadBalls: Traversable[Snowball] =
        for { Death(snowball: Snowball, _) <- sledSnowballDeaths.b } yield { snowball }
      val uniqueDeadBalls = deadBalls.toSet
      uniqueDeadBalls.foreach(_.remove())

      // collide snowballs with trees
      val snowballTreeDeaths =
        for {
          snowball <- state.snowballs.items
          nearTrees = state.trees.grid.inside(snowball.boundingBox)
          if snowballTrees(snowball, nearTrees)
        } yield snowball

      for (ball <- snowballTreeDeaths) {
        ball.remove()
      }

      // collide snowballs with each other
      val snowballDeaths =
        CollideThings
          .collideCollection(state.snowballs.items, state.snowballs.grid)
          .map(_.killed)
          .toSet

      for (snowball <- snowballDeaths) { snowball.remove() }

      // collide sleds with trees
      for {
        sled <- state.sleds.items
        nearTrees = state.trees.grid.inside(sled.boundingBox)
      } state.sledTree.collide(sled, nearTrees)

      // collide sleds with each other
      val sledDeaths =
        CollideThings.collideCollection(state.sleds.items, state.sleds.grid)

      // accumulate awards
      val snowballAwards =
        for {
          Death(killed: Sled, killer: Snowball) <- sledSnowballDeaths.a
          serverSled                            <- killer.ownerId.serverSled
          icedSled                              <- killed.id.serverSled
        } yield SledIced(serverSled, icedSled)

      val sledAwards =
        for {
          Death(killed: Sled, iced: Sled) <- sledDeaths
          serverSled                      <- iced.id.serverSled
          icedSled                        <- killed.id.serverSled
        } yield {
          SledIced(serverSled, icedSled)
        }

      val deadSnowballs = {
        val bySled =
          for (Death(killed: Snowball, killer: Sled) <- sledSnowballDeaths.b)
            yield killed.id

        snowballDeaths.map(_.id) ++ bySled ++ snowballTreeDeaths.map(_.id)
      }

      CollisionResult(snowballAwards ++ sledAwards, deadSnowballs)
    }

  /** reward the sleds and users for their achievements this round */
  private def applyAchievements[_: ParentSpan](
        achievements: Traversable[Achievement]
  ): Unit = time("applyAchievements") {
    for {
      achievement <- achievements
    } {
      achievement.sled.rewards.add(achievement)
    }
  }

  /** Track icings, to identify revenge and icing streaks */
  private def trackIcings[_: ParentSpan](
        icings: Traversable[SledIced]
  ): Traversable[Achievement] = time("trackIcings") {
    trackRevenge(icings) ++ trackIceStreaks(icings) ++ iceCountAchievements(icings)
  }

  /** A reward when total icings hit thresholds */
  private def iceCountAchievements(
        icings: Traversable[SledIced]
  ): Traversable[Achievement] = {
    for {
      SledIced(serverSled, _) <- icings
      total = serverSled.icingRecords.total
      if total > 1
      if total % iceAwardEvery == 0
    } yield {
      IceTotal(serverSled, total)
    }
  }

  /** track streaks of icing other sleds within a time period.
    * @return achievements when 2 or more sleds are iced within a period */
  private def trackIceStreaks(
        icings: Traversable[SledIced]
  ): Traversable[IcingStreak] = {
    for {
      SledIced(serverSled, _) <- icings
      if updateIceStreak(serverSled.icingRecords)
    } yield {
      logger.info(s"sled $serverSled kill streak ${serverSled.icingRecords.streak}")
      IcingStreak(serverSled, serverSled.icingRecords.streak)
    }
  }

  /** after an icing, update records to check for a spree of icings within a
    * limited time period.
    * @return true if the icing is in a spree */
  private def updateIceStreak(icingRecords: IcingRecords): Boolean = {
    icingRecords.total += 1
    icingRecords.streak += 1
    val activeStreak =
      (gameTime - icingRecords.lastTime < iceStreakPeriod
        && icingRecords.streak > 1)

    if (!activeStreak)
      icingRecords.streak = 1
    icingRecords.lastTime = gameTime
    activeStreak
  }

  /** Track history of icings, to identify revenge
    * @return revenge achievements */
  private def trackRevenge(
        icings: Traversable[SledIced]
  ): Traversable[RevengeIcing] = {
    for {
      SledIced(winningSled, losingSled) <- icings
      winningUser = winningSled.user
      losingUser  = losingSled.user
      _           = losingUser.icedBy.enqueue(winningUser)
      if winningUser.icedBy.contains(losingUser)
    } yield {
      winningUser.icedBy.removeElement(losingUser)
      val loserName = losingUser.name
      logger.info(
        s"trackIcedBy: sled ${winningSled.id} (${winningUser.name}) revenge on $loserName"
      )
      RevengeIcing(winningSled, loserName)
    }
  }

  /** Advance to the next game simulation state
    *
    * @return the time since the last time slice, in seconds
    */
  private def nextTimeSlice(): Double = {
    val currentTime  = clock.currentMillis
    val deltaSeconds = (currentTime - gameTime) / 1000.0
    lastGameTime = gameTime
    gameTime = currentTime
    deltaSeconds
  }

  private def recordTurnJitter(deltaSeconds: Double)(implicit parentSpan: Span): Unit = {
    val secondsToMicros = 1000000
    val deltaMicros     = (deltaSeconds * secondsToMicros).toLong
    Gauged("deltaMicroseconds", deltaMicros)
  }

}

object PlayfieldSteps {
  case class TurnResults(deadSleds: Traversable[SledOut],
                         deadSnowBalls: Traversable[BallId],
                         usedPowerUps: Traversable[PowerUpId],
                         newPowerUps: Traversable[PowerUp],
                         icings: Traversable[SledIced],
                         sledAchievements: Traversable[Achievement])
}
