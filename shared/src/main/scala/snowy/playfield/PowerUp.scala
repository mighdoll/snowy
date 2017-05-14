package snowy.playfield

sealed trait PowerUp extends CircularItem[PowerUp] {

  def radius: Double = 5

}

class HealthPowerUp() extends PowerUp {
  def powerUpSled(sled: Sled): Unit = {
    sled.health = sled.maxHealth
  }
}
