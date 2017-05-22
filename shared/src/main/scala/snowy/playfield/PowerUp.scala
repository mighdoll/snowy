package snowy.playfield

import vector.Vec2d

sealed trait PowerUp extends CircularItem[PowerUp] with SharedItem {

  def radius: Double = 5

  def powerUpSled(sled: Sled): Unit
}

class HealthPowerUp extends PowerUp {
  override def powerUpSled(sled: Sled): Unit = {
    sled.health = sled.maxHealth
  }
}

class SpeedPowerUp extends PowerUp {
  override def powerUpSled(sled: Sled): Unit = {
    sled.speed = sled.speed * 2
  }
}
