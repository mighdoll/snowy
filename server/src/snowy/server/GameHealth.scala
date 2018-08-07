package snowy.server

import snowy.playfield.PlayId.BallId
import snowy.playfield.Snowball
import scala.math.min
import snowy.server.rewards.Achievements.SledOut

class GameHealth(state: GameState) {
  import state.gameStateImplicits._

  /** slowly recover some health points */
  def recoverHealth(deltaSeconds: Double): Unit = {
    state.sleds.items.foreach { sled =>
      val deltaHealth = deltaSeconds / sled.healthRecoveryTime
      val newHealth   = min(sled.maxHealth, sled.health + deltaHealth)
      sled.health = newHealth
    }
  }

  /** remove old snowballs */
  def expireSnowballs(gameTime: Long): Traversable[BallId] = {
    def expired(snowball: Snowball): Boolean =
      gameTime > snowball.spawned + snowball.lifetime * 1000

    for { oldBall <- state.snowballs.items.filter(expired) } yield {
      oldBall.remove()
      oldBall.id
    }
  }

  /** @return the sleds with no health left */
  def collectDead(): Traversable[SledOut] = {
    for {
      serverSled <- state.sledMap.values
      sled = serverSled.sled
      if sled.health <= 0
    } yield {
      SledOut(serverSled)
    }
  }

}
