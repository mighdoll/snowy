package snowy.measures

import java.util.concurrent.ThreadLocalRandom

trait Measurement {
  val name: String
  val start: EpochMicroseconds = EpochMicroseconds()
  val id                       = SpanId()
  val parent: Option[Span]
  val recorder: MeasurementRecorder
}

trait CompletedMeasurement[T] extends Measurement {
  val value: T
}

trait Span extends Measurement {
  def restart(): StartedSpan = StartedSpan(name, parent, recorder, start)
}

case class CompletedSpan(
      override val id: SpanId,
      override val name: String,
      override val parent: Option[Span],
      override val recorder: MeasurementRecorder,
      override val start: EpochMicroseconds,
      end: EpochMicroseconds
) extends Span with CompletedMeasurement[Long] {
  override val value: Long = end.value - start.value
  recorder.publish(this)
}

case class StartedSpan(
      override val name: String,
      override val parent: Option[Span],
      override val recorder: MeasurementRecorder,
      override val start: EpochMicroseconds = EpochMicroseconds()
) extends Span {
  def finishNow(): CompletedSpan =
    CompletedSpan(id, name, parent, recorder, start, EpochMicroseconds())

  def rename(newName: String): StartedSpan =
    StartedSpan(newName, parent, recorder, start)

  /** time a function within this span.
    * The function is called with this span for convenience.
    */
  def finishSpan[T](fn: StartedSpan => T): T = {
    try { fn(this) }
    finally { finishNow() }
  }

  /** time a function within this span */
  def finish[T](fn: => T): T = {
    try { fn }
    finally { finishNow() }
  }

  /** time a function within a new span nested within this one */
  def time[T](name: String)(fn: => T): T = {
    val newSpan = Span(name)(this)
    try { fn }
    finally { newSpan.finishNow() }
  }

  /** time a function within a new span nested within this one.
    * The nested span is passed to the provided function.
    */
  def timeSpan[T](name: String)(fn: StartedSpan => T): T = {
    val newSpan = Span(name)(this)
    try { fn(newSpan) }
    finally { newSpan.finishNow() }
  }
}

object Span {
  def apply(name: String)(implicit parentSpan: Span): StartedSpan =
    StartedSpan(name, Some(parentSpan), parentSpan.recorder)

  def root(name: String)(implicit recorder: MeasurementRecorder): StartedSpan =
    StartedSpan(name, None, recorder)

  /** time a function, creating a new span within a parent span */
  def time[T](name: String)(fn: => T)(implicit parent: Span): T = {
    val span = Span(name)
    span.finish(fn)
  }

  /** time a function, creating a new span within a parent span */
  def timeSpan[T](name: String)(fn: StartedSpan => T)(implicit parent: Span): T = {
    val span = Span(name)
    span.finishSpan(fn)
  }
}

case class SpanId(val value: Long = ThreadLocalRandom.current.nextLong()) extends AnyVal
