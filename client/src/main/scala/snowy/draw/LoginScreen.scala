package snowy.draw

import org.scalajs.dom.window
import snowy.sleds.BasicSled
import snowy.client.ClientDraw._
import vector.Vec2d

class LoginScreen() {
  val snowFlakes = (1 to size.x.toInt / 10).map { i =>
    new SnowFlake(i * 10)
  }
  var snowLoop: Option[Int] = None

  /** Create a loop that moves and draws the snowflakes every 10ms */
  def setup(): Unit = {
    snowLoop = Some(window.setInterval(() => {
      clearScreen()

      snowFlakes.foreach { flake =>
        flake.move()
        flake.draw()
      }

      new DrawSled(
        "",
        Vec2d(size.x / 2, size.y / 2),
        size.x / 3,
        1,
        Math.PI * 3 / 2,
        Math.PI / 2,
        BasicSled,
        "rgb(120, 201, 44)")
    }, 10))
  }

  /** Stop the loop started in setup */
  def stop(): Unit = {
    snowLoop.foreach(id => window.clearInterval(id))
  }
}
