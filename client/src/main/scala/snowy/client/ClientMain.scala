package snowy.client

import scala.scalajs.js.JSApp
import org.scalajs.dom._
import snowy.draw.SnowFlakes

object ClientMain extends JSApp {
  val snowFlakes = new SnowFlakes()

  def main(): Unit = {
    snowFlakes.setup()
  }

  //When the users sends the login form, send it as a username to the server
  document.getElementById("login-form").asInstanceOf[html.Form].onsubmit = { event: Event =>
    //Connect to the WebSocket server
    new Connection(document.getElementById("username").asInstanceOf[html.Input].value)

    //Stop drawing the snow as a background
    snowFlakes.stop()

    //Do not redirect
    false
  }
}
