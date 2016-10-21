package snowy.util

import kamon.Kamon.metrics.histogram
import kamon.util.Latency
import snowy.server.GlobalConfig

object Perf {
  /** Measure the duration of a block of code and report to the monitoring system */
  def time[A](name: String)(fn: => A): A = {
    if (GlobalConfig.performanceReport) {
      Latency.measure(histogram(name))(fn)
    } else {
      fn
    }
  }

}
