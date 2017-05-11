package snowy.util

/** Import to get a pmatch method on all objects.
  *
  * Works like match, but returns the result wrapped in an option,
  * or else None if the partial function doesn't match. */
object PartialMatch {
  implicit class PartialMatcher[T](val value: T) extends AnyVal {
    def pmatch[U](pfn: PartialFunction[T, U]): Option[U] = {
      pfn.lift(value)
    }
  }
}