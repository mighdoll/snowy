package snowy.playfield

sealed trait PowerUp extends CircularItem[PowerUp] with SharedItem {
  def radius: Double = 5
}

class HealthPowerUp extends PowerUp

class SpeedPowerUp extends PowerUp
