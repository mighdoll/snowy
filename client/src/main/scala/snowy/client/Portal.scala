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

    val portalToScreenOffset = {
      val screenCenter = Vec2d(screenSize.x, screenSize.y) / 2
      val portalCenter = portalRect.size / 2
      val portalCenterScaled = portalCenter * scale
      screenCenter - portalCenterScaled
    }

    def borderWrap(pos: Vec2d): Vec2d = {
      var x = pos.x
      var y = pos.y

      val playfield = Vec2d(border.width, border.height)
      if (x > portalRect.size.x + 200) {
        x = x - playfield.x
      } else if (x < -200) {
        x = x + playfield.x
      }
      if (y > portalRect.size.x + 200) {
        y = y - playfield.y
      } else if (y < -200) {
        y = y + playfield.y
      }
      Vec2d(x, y)
    }

    def transformToScreen(playfieldObject: Vec2d): Vec2d = {
      val translateToPortal = playfieldObject - portalRect.pos
      val wrapped = borderWrap(translateToPortal)
      (wrapped * scale) + portalToScreenOffset
    }


    sleds = sleds.map { sled =>
      sled.copy(pos = transformToScreen(sled.pos))
    }
    snowballs = snowballs.map { snowball =>
      snowball.copy(pos = transformToScreen(snowball.pos))
    }
    trees = trees.map { tree =>
      tree.copy(pos = transformToScreen(tree.pos))
    }

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
