package snowy.util

/** microseconds since midnight January 1, 1970 UTC */
case class EpochMicroseconds(value: Long) extends AnyVal

/** An microsecond clock aligned within 1msec of the system clock.
  *
  * The jvm provides real time clock to milliseconds and an interval
  * timer to nanoseconds. EpochMicroseconds synthesizes a microsecond
  * real time clock by probing the alignment between the interval timer and
  * the millisecond clock.
  */
object EpochMicroseconds {

  /** microseconds since midnight January 1, 1970 UTC */
  def apply(): EpochMicroseconds = {
    val micros = approxEpochNanos() / 1000L
    EpochMicroseconds(micros)
  }

  /** nanoseconds since midnight January 1, 1970 UTC */
  def approxEpochNanos(): Long = {
    System.nanoTime() + offsetToEpochNanos
  }

  /** millieconds since midnight January 1, 1970 UTC, based on the nanosecond clock */
  def approxEpochMillis(): Long = {
    (System.nanoTime() + offsetToEpochNanos) / 1000000
  }

  // nanoseconds to add to nanoTime such that 0 is unix epoch time
  //
  // We assume that offset remains constant, and that we can henceforth
  // use the offset to convert from nanotime to wall clock time.
  private lazy val offsetToEpochNanos: Long = {
    probeOffsetToEpochNanos()
  }

  def probeOffsetToEpochNanos(): Long = {
    @inline
    def probeTime(): (Long, Long) = {
      val millis = System.currentTimeMillis()
      val nanos  = System.nanoTime()
      (millis, nanos)
    }

    def calcOffset(millis: Long, nanos: Long): Long = (millis * 1000000L) - nanos

    /** sample the milli and nano clocks until the milli clock ticks over */
    def probeUntilNextMilli(): Long = {
      var found          = 0L
      var (oldMillis, _) = probeTime()
      while (found == 0) {
        val (millis, nanos) = probeTime()
        if (millis != oldMillis) {
          found = calcOffset(millis, nanos)
        }
      }
      found
    }

    // warmup the probe loop in hopes the JIT will make it run fast
    val warmupMillis = 100
    (0 to warmupMillis).foreach(_ => probeUntilNextMilli())

    // then return the offset from the next time the milli clock ticks over
    probeUntilNextMilli()
  }

}
