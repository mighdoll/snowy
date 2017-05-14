package snowy.server

import snowy.playfield.{Playfield, Snowball}
import vector.Vec2d

class Snowballs (protected val playfield: Playfield) extends GridItems[Snowball] {
  def addBall(ball:Snowball,initialPosition:Vec2d):Unit = {
    ball.setInitialPosition(initialPosition)
    items.add(ball)
  }
}
