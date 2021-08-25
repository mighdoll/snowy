package snowy.util

import scala.math.{max, min}

object DoubleUtil {

  /** add .clip method to Double */
  implicit class ClipDouble(private val orig: Double) extends AnyVal {
    def clip(lower: Double, upper: Double): Double = {
      val lowerBounded = max(orig, lower)
      min(lowerBounded, upper)
    }
  }
}
