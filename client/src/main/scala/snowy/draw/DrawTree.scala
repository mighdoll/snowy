package snowy.draw

import snowy.client.ClientDraw.ctx
import snowy.draw.GameColors.Tree._
import snowy.playfield.Color
import vector.Vec2d

object DrawTree {
  def drawTree(pos: Vec2d, size: Double) {
    ctx.fillStyle = leaves
    drawLeaves(pos.x, pos.y, size)
    ctx.fillStyle = trunk
    drawBark(pos.x, pos.y, size)
  }
  def draw(pos: Vec2d, size: Double) {
    val x = pos.x
    val y = pos.y
    //drawCube(x, y + size * .2, size * .5, size * .5, size * .05, Color(92, 92, 92))
    drawCube(x, y, size * .15, size * .15, size * .625, Color(80, 42, 42))
    drawPyramid(
      x,
      y - size * .625,
      size * .75,
      size * .75,
      size * .75,
      Color(129, 164, 66)
    )
    drawPyramid(x, y - size * 1.25, size * .5, size * .5, size * .5, Color(155, 190, 91))
    drawPyramid(x, y - size * 2, size * .1, size * .1, size * .1, Color(245, 237, 0))
    drawPyramid(x, y - size * 2, size * .1, size * .1, -size * .1, Color(194, 187, 0))
  }

  def drawLeaves(x: Double, y: Double, size: Double) {
    ctx.beginPath()
    ctx.moveTo(x, y - size * 2)
    ctx.lineTo(x - size * 1 / 2, y - size * 1 / 2)
    ctx.lineTo(x + size * 1 / 2, y - size * 1 / 2)
    ctx.closePath()
    ctx.fill()
  }

  def drawBark(x: Double, y: Double, size: Double) {
    ctx.fillRect(x - size * 1 / 16, y - size * 3 / 4, size * 1 / 8, size * 3 / 4)
    ctx.translate(x, y - size * 29 / 50)
    ctx.rotate(Math.PI * 13 / 18)
    ctx.fillRect(0, 0, size * 1 / 20, size * 43 / 200)
    ctx.rotate(Math.PI * 5 / 9)
    ctx.fillRect(0, size * 7 / 200, size * 1 / 20, size * 1 / 4)
    ctx.fillRect(size * -1 / 10, size * -13 / 200, size * 1 / 20, size * 1 / 4)
    ctx.setTransform(1, 0, 0, 1, 0, 0)
  }

  def shadeColor(color: Color, percent: Double): String = {
    val amt = (2.55 * percent).toInt
    Color(color.r + amt, color.g + amt, color.g + amt).toString
  }

  def drawCube(x: Double, y: Double, wx: Double, wy: Double, h: Double, color: Color) {
    ctx.fillStyle = (color + -10).toString
    ctx.beginPath()
    ctx.moveTo(x, y)
    ctx.lineTo(x - wx, y - wx / 2)
    ctx.lineTo(x - wx, y - h - wx / 2)
    ctx.lineTo(x, y - h)
    ctx.closePath()
    ctx.fill()

    ctx.fillStyle = (color + 10).toString
    ctx.beginPath()
    ctx.moveTo(x, y)
    ctx.lineTo(x + wy, y - wy / 2)
    ctx.lineTo(x + wy, y - h - wy / 2)
    ctx.lineTo(x, y - h)
    ctx.closePath()
    ctx.fill()

    ctx.fillStyle = (color + 20).toString
    ctx.beginPath()
    ctx.moveTo(x, y - h)
    ctx.lineTo(x - wx, y - h - wx / 2)
    ctx.lineTo(x - wx + wy, y - h - (wx / 2 + wy / 2))
    ctx.lineTo(x + wy, y - h - wy / 2)
    ctx.closePath()
    ctx.fill()
  }

  def drawPyramid(x: Double,
                  y: Double,
                  wx: Double,
                  wy: Double,
                  h: Double,
                  color: Color) {
    ctx.fillStyle = (color + -10).toString
    ctx.beginPath()
    ctx.moveTo(x, y)
    ctx.lineTo(x - wx, y - wx / 2)
    ctx.lineTo(x - wx + wy, y - h - wy * 2 / 3)
    ctx.lineTo(x, y - h)
    ctx.closePath()
    ctx.fill()

    ctx.fillStyle = (color + 10).toString
    ctx.beginPath()
    ctx.moveTo(x, y)
    ctx.lineTo(x + wy, y - wy / 2)
    ctx.lineTo(x - wx + wy, y - h - wy * 2 / 3)
    ctx.lineTo(x, y - h)
    ctx.closePath()
    ctx.fill()
  }
}
