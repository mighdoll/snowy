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


  val halfRect = portalRect.size * .5

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

  // Todo: Fix offset bug
  def portalToScreen(x: Double, y: Double): Portal = {
    val scaleX = x / portalRect.size.x
    val scaleY = y / portalRect.size.y
    scale = math.max(scaleX, scaleY)

    val antiParallax = (portalRect.pos + halfRect) - ((portalRect.pos + halfRect) * scale)
    def scaleObjectPos[A <: PlayfieldObject](playfieldObject: A): playfieldObject.MyType = {
      val sledScale = playfieldObject.pos * scale - antiParallax
      playfieldObject.updatePos(sledScale)
    }
    sleds = sleds.map(scaleObjectPos(_))
    snowballs = snowballs.map(scaleObjectPos(_))
    trees = trees.map(scaleObjectPos(_))

    this
  }
}
