package snowy.playfield

import scala.concurrent.duration.FiniteDuration
import upickle.default.ReadWriter

/** A value (e.g. a maxSpeed boost) that decays over a period of game time */
case class DecayingBoost(
      var boostStart: Long = 0,
      var boostEnd: Long = 0,
      var boostAmount: Int = 0
) derives ReadWriter {
  private def length: Long = boostEnd - boostStart

  /** set the boost value to maximum and define the decay time */
  def start(amount: Int, duration: FiniteDuration, startTime: Long): Unit = {
    boostStart = startTime
    boostEnd = boostStart + duration.toMillis
    boostAmount = amount
  }

  /** set the boost value to zero */
  def stop(): Unit = boostStart = 0

  /** @return the current value of the boost */
  def current(gameTime: Long): Int = {
    if (gameTime > boostEnd) stop()

    if (boostStart > 0 && gameTime < boostEnd && gameTime >= boostStart) {
      val progress: Double  = (gameTime - boostStart) / length.toDouble
      val scale             = easeOutExpo(progress)
      val interpolatedValue = math.round(boostAmount * scale).toInt
      interpolatedValue
    } else {
      0
    }
  }

  /** Steady then steeply decaying interpolation function. input from 0 to 1
    * @return
    *   eased function
    */
  private def easeOutExpo(x: Double): Double = {
    assert(x >= 0)
    assert(x <= 1)
    1 - math.pow(2, 10 * (x - 1))
  }
}
