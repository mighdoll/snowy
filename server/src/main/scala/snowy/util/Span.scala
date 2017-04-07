package snowy.util

import java.util.concurrent.ThreadLocalRandom

trait Measurement {
  val name: String
  val start: EpochMicroseconds
}

trait Span {
  val name: String
  val parent: Option[Span]
  val recorder: MeasurementRecorder
  val id                     = SpanId()
  def restart(): StartedSpan = StartedSpan(name, parent, recorder)
}

case class CompletedSpan(
      override val id: SpanId,
      override val name: String,
      override val parent: Option[Span],
      override val recorder: MeasurementRecorder,
      start: EpochMicroseconds,
      end: EpochMicroseconds
) extends Span with Measurement {
  recorder.publish(this)
}

case class StartedSpan(
      override val name: String,
      override val parent: Option[Span],
      override val recorder: MeasurementRecorder,
      start: EpochMicroseconds = EpochMicroseconds()
) extends Span {
  def finish(): CompletedSpan =
    CompletedSpan(id, name, parent, recorder, start, EpochMicroseconds())

  def rename(newName: String): StartedSpan =
    StartedSpan(newName, parent, recorder, start)

  def timeSpan[T](fn: StartedSpan => T): T = {
    try { fn(this) } finally { finish() }
  }

  def time[T](fn: => T): T = {
    try { fn } finally { finish() }
  }
}

object Span {
  def apply(name: String)(implicit parentSpan: Span): StartedSpan =
    StartedSpan(name, Some(parentSpan), parentSpan.recorder)

  def root(name: String)(implicit recorder: MeasurementRecorder): StartedSpan =
    StartedSpan(name, None, recorder)

  def time[T](name: String)(fn: => T)(implicit parent: Span): T = {
    val span = Span(name)
    span.time(fn)
  }
}

case class SpanId(val value: Long = ThreadLocalRandom.current.nextLong()) extends AnyVal
