package snowy.client

import snowy.playfield._
import vector.Vec2d

class Portal(portalRect: Rect, screenSize: Vec2d, border: Vec2d) {
  private val scaleX = screenSize.x / portalRect.size.x
  private val scaleY = screenSize.y / portalRect.size.y
  val scale          = math.max(scaleX, scaleY)

  private val portalToScreenOffset = {
    val portalScaled = portalRect.size * scale
    (screenSize - portalScaled) / 2
  }

  private val treeBBox = Vec2d(100, 200)

  /** @param pos of a sled snowball or tree
    * @return pos translated, wrapped, and filtered */
  def transformToScreen(pos: Vec2d): Option[Vec2d] = {
    val portalPos = pos - portalRect.pos

    for {
      portalPosition <- wrapInPlayfield(portalPos, treeBBox, border)
    } yield {
      portalPosition * scale + portalToScreenOffset
    }
  }
  def tryWrap(value: Double, max: Double, pad: Double, wrapSize: Double): Double = {
      if (value > pad + max) value - wrapSize
      else if (value < -pad) value + wrapSize
      else value
  }
  /** @return a position mapped to portalrect coordinates */
  private def wrapInPlayfield(pos: Vec2d,
                              padding: Vec2d,
                              wrapRange: Vec2d): Option[Vec2d] = {

    /** TODO take min, max, wrapSize instead of pad? */
    def wrap(value: Double, max: Double, pad: Double, wrapSize: Double): Option[Double] = {
      val wrapped = tryWrap(value, max, pad, wrapSize)

      if (wrapped > -pad && wrapped < pad + max)
        Some(wrapped)
      else {
        None
      }
    }
    for {
      x <- wrap(pos.x, portalRect.size.x, padding.x, wrapRange.x)
      y <- wrap(pos.y, portalRect.size.y, padding.y, wrapRange.y)
    } yield {
      Vec2d(x, y)
    }
  }
}
