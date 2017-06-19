package snowy.server

import snowy.Awards._
import snowy.playfield.PlayId.BallId
import snowy.playfield.Snowball

import scala.math.min

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
  def expireSnowballs(): Traversable[BallId] = {
    val now = System.currentTimeMillis()
    def expired(snowball: Snowball): Boolean =
      now > snowball.spawned + snowball.lifetime * 1000

    for { oldBall <- state.snowballs.items.filter(expired) } yield {
      oldBall.remove()
      oldBall.id
    }
  }

  /** @return the sleds with no health left */
  def collectDead(): Traversable[SledDied] = {
    state.sleds.items.find(_.health <= 0).map { sled =>
      SledDied(sled.id)
    }
  }

}
