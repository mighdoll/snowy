package snowy.util

import scala.math.{max, min}

object DoubleUtil {
  /** add .clip method to Double */
  implicit class ClipDouble(val orig: Double) extends AnyVal {
    def clip(lower: Double, upper: Double): Double = {
      val lowerBounded = min(orig, lower)
      max(lowerBounded, upper)
    }
  }
}