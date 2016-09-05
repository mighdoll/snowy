import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom._
import GameClientProtocol._
import ClientDraw._

object ClientMain extends JSApp {

  case class Size(width: Int, height: Int)

  var size = Size(window.innerWidth, window.innerHeight)

  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  gameCanvas.width = size.width
  gameCanvas.height = size.height
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  var snowLoop: Option[Int] = None
  var gTrees = Trees(Vector(Tree(20, Position(446, 69))))
  var gPlayField = PlayField(0, 0)

  var snowFlakes = (1 to size.width / 10).map(i => new snowFlake(ctx, i, size))

  def draw() = {
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()

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
    document.getElementById("game-div").asInstanceOf[html.Div].className = "fullscreen"
    document.getElementById("start-div").asInstanceOf[html.Div].className = "back fullscreen"

    //Stop drawing the snow as a background
    snowLoop.foreach { id => window.clearInterval(id) }

    //Do not redirect
    false
  }

  def main(): Unit = {
    //Start the background loop
    snowLoop = Some(window.setInterval(draw _, 10))
  }

  window.onresize = (_: UIEvent) => {
    size = Size(window.innerWidth, window.innerHeight)
    gameCanvas.width = size.width
    gameCanvas.height = size.height
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()
  }
}
