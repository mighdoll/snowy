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

  // nanoseconds to add to nanoTime such that 0 is unix epoch time
  //
  // We assume that offset remains constant, and that we can henceforth
  // use the offset to convert from nanotime to wall clock time.
  private lazy val offsetToEpochNanos: Long = {
    val millis = System.currentTimeMillis()
    val nanos = System.nanoTime()
    val nanoClockOffset = (millis * 1000000L) - nanos
    nanoClockOffset
  }

}
