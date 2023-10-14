package snowy.server.rewards

import snowy.GameConstants.{speedPowerUp, speedPowerUpDuration}
import snowy.playfield.{HealthPowerUp, PowerUp, SpeedPowerUp}

import scala.concurrent.duration.*

object PowerUpRewards {
  def reward(powerUp: PowerUp): Reward = {
    powerUp match {
      case _: HealthPowerUp => FullHealth
      case _: SpeedPowerUp  => TemporarySpeed(speedPowerUp, speedPowerUpDuration.seconds)
    }
  }
}
