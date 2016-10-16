package snowy.draw

import snowy.client.ClientDraw._
import vector.Vec2d
import org.scalajs.dom.window

class LoginScreen() {
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

      new DrawSled("", Vec2d(size.width / 2, size.height / 2), size.width / 3, 1, Math.PI * 3 / 2, Math.PI / 2, "rgb(120, 201, 44)")
    }, 10))
  }

  /** Stop the loop started in setup */
  def stop(): Unit = {
    snowLoop.foreach(id => window.clearInterval(id))
  }
}