package snowy.util

import scala.collection.mutable

/** a FIFO queue with a limited number of elements */
class FiniteQueue[A](maxSize: Int) extends mutable.Queue[A] {
  override def +=(elem: A): this.type = {
    while (length >= maxSize) {
      dequeue()
    }
    super.+=(elem)
  }
}
