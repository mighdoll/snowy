package snowy.playfield

object Intersect {
  implicit class RectOps(private val rect: Rect) extends AnyVal {

    /** check if two axis-aligned rectangles intersect. Assumes larger y values go down.
      * @return true if the two rectangles intersect
      * */
    def intersectRect(other: Rect): Boolean = {
      //    val noOverlap = (a.right < b.left) || (b.right < a.left) || (a.bottom < b.top) || (b.bottom < a.top)

      // inverse of noOverlap:
      val intersects =
        (rect.right >= other.left) &&
          (other.right >= rect.left) &&
          (rect.bottom >= other.top) &&
          (other.bottom >= rect.top)

      intersects
    }
  }
}
