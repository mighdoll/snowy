package snowy.util

import scala.concurrent.duration.FiniteDuration

object RateLimit {

  /** @return a function that relays calls to a provided function no more than
    * once per period
    * @param finiteDuration minimum time between calls
    * @param fn function to call
    */
  def rateLimit(finiteDuration: FiniteDuration)(fn: => Unit): () => Unit = {
    var started  = false
    var lastTime = 0L

    () =>
      {
        val current = System.nanoTime()
        if (!started || current - lastTime > finiteDuration.toNanos) {
          started = true
          lastTime = current
          fn
        }
      }
  }
}
