package snowy.draw

import snowy.client.ClientDraw.{ctx, size}
import snowy.client.Portal
import snowy.draw.GameColors.Sled._
import snowy.draw.GameColors.clearColor
import snowy.playfield.{Rect, Sled, Store, Tree}
import vector.Vec2d

class DrawMiniMap(border: Vec2d) {

  val miniscale  = Math.max(border.x / size.x, border.y / size.y) * 2
  val miniscaled = border / miniscale
  val minipos    = size * .99 - miniscaled

  def draw(mySled: Sled, trees: Store[Tree]) {
    val rawminimap = new Portal(
      Rect(
        Vec2d(0, 0),
        border
      )
    )(Set(mySled), Set(), trees.items)
    val minimap = rawminimap.convertToScreen(border / miniscale, border)

    ctx.fillStyle = "rgba(255, 255, 255, 0.5)"
    ctx.fillRect(minipos.x, minipos.y, miniscaled.x, miniscaled.y)

    minimap.trees.foreach { tree =>
      val newPos = tree.pos + minipos
      new DrawTree(newPos, 50 * minimap.scale)
    }

    minimap.sleds.foreach { sled =>
      val newPos = sled.pos + minipos
      val size   = 150 * minimap.scale
      ctx.fillStyle = bodyGreen
      ctx.beginPath()
      ctx.moveTo(newPos.x, newPos.y)
      ctx.bezierCurveTo(
        newPos.x - size,
        newPos.y - size * 1.5,
        newPos.x + size,
        newPos.y - size * 1.5,
        newPos.x,
        newPos.y)
      ctx.closePath()
      ctx.fill()

      ctx.fillStyle = clearColor.toString
      ctx.beginPath()
      ctx.arc(newPos.x, newPos.y - size * 0.7, size * 0.2, 0, math.Pi * 2)
      ctx.fill()
    }
  }
}
