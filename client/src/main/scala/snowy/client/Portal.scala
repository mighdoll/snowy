package snowy.client

import snowy.GameClientProtocol._
import snowy.GameConstants
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


  // TODO comment
  private def wrapInPlayfield(pos:Vec2d, padding:Vec2d, wrapRange:Vec2d):Option[Vec2d] = {

    /** TODO take min, max, wrapSize instead of pad? */
    def wrap(value:Double, max:Double, pad:Double, wrapSize:Double):Option[Double] = {
      val wrapped =
        if (value > pad + max) { value - wrapSize }
        else if (value < -pad) { value + wrapSize }
        else value

      if (wrapped > -pad && wrapped < pad + max)
        Some(wrapped)
      else {
//        println(s"wrapInPlayfield. filtering: $value   max:$max   pad:$pad   wrapSize:$wrapSize")
        None
      }
    }

    for {
      x <- wrap(pos.x, portalRect.size.x, padding.x, wrapRange.x)
      y <- wrap(pos.y, portalRect.size.y, padding.y, wrapRange.y)
    } yield {
      Vec2d(x,y)
    }
  }


  def convertToScreen(screenSize: Vec2d, border: Playfield): Portal = {
    val scaleX = screenSize.x / portalRect.size.x
    val scaleY = screenSize.y / portalRect.size.y
    scale = math.max(scaleX, scaleY)

    val treeSize = 200 * scale // Don't clip any partially off trees
    val treeBox = Vec2d(200, 200) // TODO treeSize seems right in theory, but not in practice.

    val portalToScreenOffset = {
      val portalScaled = portalRect.size * scale
      (screenSize - portalScaled) / 2
    }

    /*
    def wrap2d(pos: Double, size: Double, border: Double): Option[Double] = {
      val wrap = pos match {
        case i if i > size + treeSize => pos - border
        case i if i < -treeSize       => pos + border
        case _                        => pos
      }
      // Doesn't do anything yet
      wrap match {
        case i if i > size + treeSize => None
        case i if i < -treeSize       => None
        case _                        => Some(wrap)
      }
    }
    */


    /** @param playfieldObject a sled snowball or tree
      * @return playfieldObject translated, wrapped, and filtered */
    def transformToScreen[A <: PlayfieldObject](playfieldObject: A): Option[playfieldObject.MyType] = {
      val portalPos = playfieldObject.pos - portalRect.pos

      /*
      val wrappedX = wrap2d(translateToPortal.x, portalRect.size.x, border.width)
      val wrappedY = wrap2d(translateToPortal.y, portalRect.size.y, border.height)


      if (wrappedX.isDefined && wrappedY.isDefined) {
        val wrapped = Vec2d(wrappedX.get, wrappedY.get)
        val last = wrapped * scale + portalToScreenOffset
        Some(playfieldObject.updatePos(last))
      } else {
        None
      }
      */

      for {
        // TODO use dynamic Playfield rather than GameConstants.playfield
        portalPosition <- wrapInPlayfield(portalPos, treeBox, GameConstants.playfield)
      } yield {
        val newPos = portalPosition * scale + portalToScreenOffset
        playfieldObject.updatePos(newPos)
      }
    }

    trees = trees.flatMap(transformToScreen(_))
    sleds = sleds.flatMap(transformToScreen(_))
    snowballs = snowballs.flatMap(transformToScreen(_))

    this
  }
}
