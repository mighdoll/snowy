package snowy.util

case class Gauged[T](override val name: String,
                     val value: T,
                     val parentSpan: Span,
                     override val start: EpochMicroseconds = EpochMicroseconds(),
                     val id: SpanId = SpanId())
    extends Measurement

object Gauged {
  def apply[T](name: String, value: T)(implicit parentSpan: Span): Unit = {

    val gauged = Gauged(name, value, parentSpan)
    parentSpan.recorder.publish(gauged)
  }
}
