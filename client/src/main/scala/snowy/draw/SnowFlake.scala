package snowy.draw

import snowy.client.ClientDraw._
import vector.Vec2d

class SnowFlake(index: Double) {
  val flakeSize = Math.random() * 5 + 5
  var pos = Vec2d(index, -Math.random() * size.y)

  def move(): Unit = {
    pos = pos.copy(y = pos.y + flakeSize / 5)
    pos = pos.copy(y = pos.y % size.y)
  }

  def draw(): Unit = {
    new DrawSnowball(pos, flakeSize)
  }
}
