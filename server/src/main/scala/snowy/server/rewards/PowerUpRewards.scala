package snowy.server.rewards

import snowy.playfield.{HealthPowerUp, PowerUp, SpeedPowerUp}
import scala.concurrent.duration._

object PowerUpRewards {
  def reward(powerUp: PowerUp): Reward = {
    powerUp match {
      case _: HealthPowerUp => FullHealth
      case _: SpeedPowerUp  => TemporarySpeed(100, 10.seconds)
    }
  }
}
