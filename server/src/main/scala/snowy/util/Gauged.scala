package snowy.util

case class Gauged[T](override val name: String,
                     override val value: T,
                     val parentSpan: Span)
    extends CompletedMeasurement[T] {
  override val parent   = Some(parentSpan)
  override val recorder = parentSpan.recorder
}

object Gauged {
  def apply[T](name: String, value: T)(implicit parentSpan: Span): Unit = {

    val gauged = Gauged(name, value, parentSpan)
    parentSpan.recorder.publish(gauged)
  }
}
