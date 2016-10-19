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


  def translateToPortal(): Portal = {
    def translateToPortal[A <: PlayfieldObject](playfieldObject: A): playfieldObject.MyType = {
      val sledMoved = playfieldObject.pos - portalRect.pos
      playfieldObject.updatePos(sledMoved)
    }
    sleds = sleds.map(translateToPortal(_))
    snowballs = snowballs.map(translateToPortal(_))
    trees = trees.map(translateToPortal(_))

    // Todo: Must allow trees on the other side of border
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

  def portalToScreen(screenWidth: Double, screenHeight: Double): Portal = {
    val scaleX = screenWidth / portalRect.size.x
    val scaleY = screenHeight / portalRect.size.y
    scale = math.max(scaleX, scaleY)

    val portalToScreenOffset = {
      val screenCenter = Vec2d(screenWidth, screenHeight) / 2
      val portalCenter = portalRect.size / 2
      val portalCenterScaled = portalCenter * scale
      screenCenter - portalCenterScaled
    }

    def scaleObjectPos[A <: PlayfieldObject](playfieldObject: A): playfieldObject.MyType = {
      val newPos = (playfieldObject.pos * scale) + portalToScreenOffset
      playfieldObject.updatePos(newPos)
    }
    sleds = sleds.map(scaleObjectPos(_))
    snowballs = snowballs.map(scaleObjectPos(_))
    trees = trees.map(scaleObjectPos(_))

    this
  }
}
