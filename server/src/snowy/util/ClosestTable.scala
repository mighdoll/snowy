package snowy.util

/** A table of values indexed sequentially (like an array). The starting
  * index can be any integer not just zero. Index fetches outside the mapped
  * range are clamped to first or last value. */
class ClosestTable[A](baseIndex: Int, elements: A*) {
  assert(elements.nonEmpty)
  val maxIndex = elements.length - 1 + baseIndex

  def get(index: Int): A = {
    if (index < baseIndex) {
      elements.head
    } else if (index > maxIndex) {
      elements.last
    } else {
      elements(index - baseIndex)
    }
  }
}