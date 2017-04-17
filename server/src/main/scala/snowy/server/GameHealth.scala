package snowy.server

import scala.math.min
import snowy.Awards._
import snowy.playfield.PlayId.BallId
import snowy.playfield.Snowball

class GameHealth(state: GameState) {

  /** slowly recover some health points */
  def recoverHealth(deltaSeconds: Double): Unit = {
    state.sleds.items.foreach { sled =>
      val deltaHealth = deltaSeconds / sled.healthRecoveryTime
      val newHealth   = min(sled.maxHealth, sled.health + deltaHealth)
      sled.health = newHealth
    }
  }

  /** remove old snowballs */
  def expireSnowballs(): Traversable[BallId] = {
    val now = System.currentTimeMillis()
    def expired(snowball: Snowball): Boolean =
      now > snowball.spawned + snowball.lifetime * 1000

    val oldBalls = state.snowballs.items filter expired map (_.id)
    state.snowballs = state.snowballs removeMatchingItems expired
    oldBalls
  }

  /** @return the sleds with no health left */
  def collectDead(): Traversable[SledDied] = {
    state.sleds.items.find(_.health <= 0).map { sled =>
      SledDied(sled.id)
    }
  }

}
