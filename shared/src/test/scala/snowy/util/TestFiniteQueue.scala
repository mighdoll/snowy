package snowy.util

import org.scalatest.PropSpec

class TestFiniteQueue extends PropSpec {
  property("adding 3 items to a length 2 queue drops one of them") {
    val fq = new FiniteQueue[Int](2)
    fq.enqueue(1,2,3)
    assert(fq.length === 2)
    assert(fq.dequeue() === 2)
  }
}
