package snowy.util

import kamon.Kamon.metrics.histogram
import kamon.util.Latency

object Perf {
  /** Measure the duration of a block of code and report to the monitoring system */
  def time[A](name: String)(fn: => A): A = {
    Latency.measure(histogram(name))(fn)
  }

}
