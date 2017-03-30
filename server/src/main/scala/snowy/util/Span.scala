package snowy.util

import java.util.concurrent.ThreadLocalRandom

trait Measurement {
  val name: String
  val start: EpochMicroseconds
}

trait Span {
  val name: String
  val recorder: MeasurementRecorder
  val id                     = SpanId()
  def restart(): StartedSpan = StartedSpan(name, recorder)
}

case class CompletedSpan(
      name: String,
      recorder: MeasurementRecorder,
      start: EpochMicroseconds,
      end: EpochMicroseconds
) extends Span with Measurement {
  recorder.publish(this)
}

case class StartedSpan(
      name: String,
      recorder: MeasurementRecorder,
      start: EpochMicroseconds = EpochMicroseconds()
) extends Span {
  def finish(): CompletedSpan              = CompletedSpan(name, recorder, start, EpochMicroseconds())
  def rename(newName: String): StartedSpan = StartedSpan(newName, recorder, start)
}

object Span {
  def start(name: String)(implicit recorder: MeasurementRecorder): StartedSpan =
    StartedSpan(name, recorder)
}

case class SpanId(val value: Long = ThreadLocalRandom.current.nextLong()) extends AnyVal
