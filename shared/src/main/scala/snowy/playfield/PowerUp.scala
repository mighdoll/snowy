package snowy.playfield

trait InSharedSet

sealed trait PowerUp extends CircularItem[PowerUp] with InSharedSet {

  def radius: Double = 5

}

class HealthPowerUp() extends PowerUp {
  def powerUpSled(sled: Sled): Unit = {
    sled.health = sled.maxHealth
  }
}
