package snowy.client

import snowy.playfield._
import vector.Vec2d


class Portal(rect: Rect) {
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


  val halfRect = rect.size * .5

  def translateToScreen(): Portal = {
    def translate[A <: PlayfieldObject](x: A): x.MyType = {
      val sledMoved = x.pos - rect.pos + halfRect
      x.updatePos(sledMoved)
    }
    sleds = sleds.map(translate(_))
    snowballs = snowballs.map(translate(_))
    trees = trees.map(translate(_))

    // Todo: Must allow trees on the other side of border
    def filterOut[A <: PlayfieldObject](size: Vec2d) = (x: A) => {
      val treeSize = 200 // Don't clip any partially off trees

      (x.pos.x > -treeSize && x.pos.y > -treeSize) &&
        (x.pos.x < size.x + treeSize && x.pos.y < size.y + treeSize)
    }
    trees = trees.filter(filterOut(rect.size))
    snowballs = snowballs.filter(filterOut(rect.size))
    sleds = sleds.filter(filterOut(rect.size))

    this
  }

  // Todo: Fix offset bug
  def resizeToScreen(x: Double, y: Double): Portal = {
    val scaleX = x / rect.size.x
    val scaleY = y / rect.size.y

    scale = if (scaleX < scaleY) scaleY else scaleX

    val shift = (rect.size - Vec2d(x, y)) / 2

    def resize[A <: PlayfieldObject](x: A): x.MyType = {
      val sledOffset = x.pos - shift
      val sledScale = sledOffset * scale
      x.updatePos(sledScale)
    }
    sleds = sleds.map(resize(_))
    snowballs = snowballs.map(resize(_))
    trees = trees.map(resize(_))

    this
  }
}
