package snowy.util

import scala.math.{max, min}

object IntUtil {

  /** add .clip method to Int */
  implicit class ClipInt(private val orig: Int) extends AnyVal {
    def clip(lower: Int, upper: Int): Int = {
      val lowerBounded = max(orig, lower)
      min(lowerBounded, upper)
    }
  }
}
