import scala.scalajs.js.JSApp
import ClientDraw._
import GameClientProtocol._
import org.scalajs.dom._

object ClientMain extends JSApp {

  var snowLoop: Option[Int] = None
  var gTrees = Trees(Vector(Tree(20, Position(446, 69))))
  var gPlayField = PlayField(0, 0)
  var snowFlakes = (1 to size.width / 10).map(i => new snowFlake(i))

  def main(): Unit = {
    //Start the background loop
    snowLoop = Some(window.setInterval(draw _, 10))
  }

  def draw() = {
    clearScreen()

    snowFlakes.foreach { flake =>
      flake.move()
      flake.draw()
    }
  }

  //When the users sends the login form, send it as a username to the server
  document.getElementById("login-form").asInstanceOf[html.Form].onsubmit = { event: Event =>
    //Connect to the WebSocket server
    new Connection(document.getElementById("username").asInstanceOf[html.Input].value, size, ctx)

    //Swap front and back panes
    document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
    document.getElementById("start-div").asInstanceOf[html.Div].classList.add("back")

    //Stop drawing the snow as a background
    snowLoop.foreach { id => window.clearInterval(id) }

    //Do not redirect
    false
  }

  case class Size(width: Int, height: Int)

}
