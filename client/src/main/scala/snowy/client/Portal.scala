package snowy.client

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


  def convertToScreen(screenSize: Vec2d): Portal = {
    val scaleX = screenSize.x / portalRect.size.x
    val scaleY = screenSize.y / portalRect.size.y
    scale = math.max(scaleX, scaleY)

    val portalToScreenOffset = {
      val screenCenter = Vec2d(screenSize.x, screenSize.y) / 2
      val portalCenter = portalRect.size / 2
      val portalCenterScaled = portalCenter * scale
      screenCenter - portalCenterScaled
    }

    def transformToScreen[A <: PlayfieldObject](playfieldObject: A): playfieldObject.MyType = {
      val translateToPortal = playfieldObject.pos - portalRect.pos
      val newPos = (translateToPortal * scale) + portalToScreenOffset
      playfieldObject.updatePos(newPos)
    }
    sleds = sleds.map(transformToScreen(_))
    snowballs = snowballs.map(transformToScreen(_))
    trees = trees.map(transformToScreen(_))

    def filterOut[A <: PlayfieldObject](size: Vec2d) = (playfieldObject: A) => {
      val treeSize = 200 // Don't clip any partially off trees

      (playfieldObject.pos.x > -treeSize && playfieldObject.pos.y > -treeSize) &&
        (playfieldObject.pos.x < size.x + treeSize && playfieldObject.pos.y < size.y + treeSize)
    }
    trees = trees.filter(filterOut(portalRect.size))
    snowballs = snowballs.filter(filterOut(portalRect.size))
    sleds = sleds.filter(filterOut(portalRect.size))
    

    this
  }
}
