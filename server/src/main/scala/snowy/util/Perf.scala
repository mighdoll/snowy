package snowy.util

import snowy.server.GlobalConfig

object Perf {

  /** Measure the duration of a block of code and report to the monitoring system */
  def time[A](name: String)(fn: => A): A = {
    if (enabled) {
      fn
    } else {
      fn
    }
  }

  /** record a single value in a histogram */
  def record(name: String, value: Long): Unit = {
//    if (enabled) histogram(name).record(value)
  }

  /** True if performance tracking is enabled */
  def enabled: Boolean = GlobalConfig.performanceReport

  /** start the performance tracking subsystem */
  def start(): Unit = {
//    if (enabled) Kamon.start()
  }

}
