package snowy.playfield

import scala.concurrent.duration.FiniteDuration

/** A value (e.g. a maxSpeed boost) that decays over a period of game time */
class DecayingBoost() {
  private var start: Long  = 0
  private var end: Long    = 0
  private var boost: Long  = 0
  private def length: Long = end - start

  /** set the boost value to maximum and define the decay time */
  def start(amount: Int, duration: FiniteDuration, startTime: Long): Unit = {
    start = startTime
    end = start + duration.toMillis
    boost = amount
  }

  /** set the boost value to zero */
  def stop(): Unit = start = 0

  /** @return the current value of the boost */
  def current(gameTime: Long): Int = {
    if (gameTime > end) stop()

    if (start > 0 && gameTime < end && gameTime >= start) {
      val progress: Double  = (gameTime - start) / length.toDouble
      val scale             = easeOutExpo(progress)
      val interpolatedValue = math.round(boost * scale).toInt
      interpolatedValue
    } else {
      0
    }
  }

  /** Steady then steeply decaying interpolation function.
    * input from 0 to 1
    * @return eased function
    */
  private def easeOutExpo(x: Double): Double = {
    assert(x >= 0)
    assert(x <= 1)
    1 - math.pow(2, 10 * (x - 1))
  }
}
