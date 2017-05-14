package snowy.server

import snowy.playfield.{HealthPowerUp, Playfield, PowerUp}

/** A managed collection of PowerUps on the playfield */
class PowerUps(protected val playfield: Playfield) extends GridItems[PowerUp] {
  private val areaPerPowerUp = 600 * 600
  private val area           = playfield.size.x.toInt * playfield.size.y.toInt
  private val targetCount    = area / areaPerPowerUp

  items ++= initialPowerUps()

  private def initialPowerUps(): Set[PowerUp] = {
    (1 to targetCount).map { _ =>
      val powerUp = new HealthPowerUp()
      powerUp.position = playfield.randomSpot()
      powerUp
    }.toSet
  }
}
