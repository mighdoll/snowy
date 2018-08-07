package snowy.server.rewards

import snowy.playfield.{HealthPowerUp, PowerUp, SpeedPowerUp}
import scala.concurrent.duration._
import snowy.GameConstants.{speedPowerUp, speedPowerUpDuration}

object PowerUpRewards {
  def reward(powerUp: PowerUp): Reward = {
    powerUp match {
      case _: HealthPowerUp => FullHealth
      case _: SpeedPowerUp  => TemporarySpeed(speedPowerUp, speedPowerUpDuration.seconds)
    }
  }
}
