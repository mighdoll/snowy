package snowy.server

import snowy.playfield.{Playfield, Snowball}
import vector.Vec2d

class Snowballs(protected val playfield: Playfield) extends GridItems[Snowball] {

  /** add a new snowball to the playfield */
  def addBall(ball: Snowball, initialPosition: Vec2d): Unit = {
    val position = playfield.wrapInPlayfield(initialPosition)
    ball.setInitialPosition(position)
    items.add(ball)
  }
}
