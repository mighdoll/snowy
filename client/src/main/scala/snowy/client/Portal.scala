package snowy.client

import snowy.GameClientProtocol._
import snowy.playfield._
import vector.Vec2d

class Portal(portalRect: Rect) {
  var sleds = Seq[Sled]()
  var trees = Seq[Tree]()
  var snowballs = Seq[Snowball]()
  var scale = 0.0

  def apply(tsleds: Set[Sled], tsnowballs: Set[Snowball], ttrees: Set[Tree]): Portal = {
    sleds = tsleds.toSeq
    snowballs = tsnowballs.toSeq
    trees = ttrees.toSeq
    this
  }


  def convertToScreen(screenSize: Vec2d, border: Playfield): Portal = {
    val scaleX = screenSize.x / portalRect.size.x
    val scaleY = screenSize.y / portalRect.size.y
    scale = math.max(scaleX, scaleY)

    val treeSize = 200 * scale // Don't clip any partially off trees

    val portalToScreenOffset = {
      val portalScaled = portalRect.size * scale
      (screenSize - portalScaled) / 2
    }

    def wrap2d(pos: Double, size: Double, border: Double): Option[Double] = {
      var p = pos
      p match {
        case i if i > size + treeSize => p = p - border
        case i if i < -treeSize       => p = p + border
        case _                        =>
      }
      // Doesn't do anything yet
      p match {
        case i if i > size + treeSize => None
        case i if i < -treeSize       => None
        case _                        => Some(p)
      }
    }

    /** @param playfieldObject a sled snowball or tree
      * @return playfieldObject translated, wrapped, and filtered */
    def transformToScreenFilter[A <: PlayfieldObject](playfieldObject: A): Option[playfieldObject.MyType] = {
      val translateToPortal = playfieldObject.pos - portalRect.pos

      val wrappedX = wrap2d(translateToPortal.x, portalRect.size.x, border.width)
      val wrappedY = wrap2d(translateToPortal.y, portalRect.size.y, border.height)

      if (wrappedX.isDefined && wrappedY.isDefined) {
        val wrapped = Vec2d(wrappedX.get, wrappedY.get)
        val last = wrapped * scale + portalToScreenOffset
        Some(playfieldObject.updatePos(last))
      } else {
        None
      }
    }

    sleds = sleds.flatMap(transformToScreenFilter(_))
    snowballs = snowballs.flatMap(transformToScreenFilter(_))
    trees = trees.flatMap(transformToScreenFilter(_))

    this
  }
}
