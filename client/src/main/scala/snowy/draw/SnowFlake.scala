package snowy.draw

import snowy.client.ClientDraw._
import vector.Vec2d
import org.scalajs.dom.window

class SnowFlake(index: Double) {
  val flakeSize = Math.random() * 5 + 5
  var pos = Vec2d(index, -Math.random() * size.height)

  def move(): Unit = {
    pos = pos.copy(y = pos.y + flakeSize / 5)
    pos = pos.copy(y = pos.y % size.height)
  }

  def draw(): Unit = {
    new DrawSnowball(pos, flakeSize)
  }
}

class SnowFlakes() {
  var snowLoop: Option[Int] = None
  val snowFlakes = (1 to size.width / 10).map { i =>
    new SnowFlake(i * 10)
  }

  /** Create a loop that moves and draws the snowflakes every 10ms */
  def setup(): Unit = {
    snowLoop = Some(window.setInterval(() => {
      clearScreen()

      snowFlakes.foreach { flake =>
        flake.move()
        flake.draw()
      }
    }, 10))
  }

  /** Stop the loop started in setup */
  def stop(): Unit = {
    snowLoop.foreach(id => window.clearInterval(id))
  }
}