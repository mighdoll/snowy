import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom._
import upickle.default._
import GameServerProtocol._
import GameClientProtocol._

object TryMe extends JSApp {
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  gameCanvas.width = size.width
  gameCanvas.height = size.height
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  var snowLoop:Option[Int] = None

  object size {
    val width = window.innerWidth
    val height = window.innerHeight
  }

  var time = 0
  def draw() = {
    //Simple drawing with time
    //TODO: Replace with snow
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()

    ctx.beginPath()
    ctx.moveTo(10, 10)
    ctx.lineTo(
      size.width - (time%size.width),
      size.height - (time%size.height)
    )
    ctx.stroke()

    time += 1
  }

  def connect() = {
    val socket = new WebSocket("ws://localhost:2345/game")

    val join = Join(document.getElementById("username").asInstanceOf[html.Input].value)
    val msg = write(join)

    socket.onopen = {event:Event =>
      socket.send(msg)
    }
    socket.onerror = { event: ErrorEvent =>
      console.log(s"Failed: code: ${event}")
    }
    socket.onclose = { event: Event =>
      console.log(s"socket closed ")
    }

    socket.onmessage = { event: MessageEvent =>
      val msg = event.data.toString
      console.log(s"received message: $msg")

      read[GameClientMessage](msg) match {
        case state:State => receivedState(state)
        case playfield:PlayField => receivedPlayField(playfield)
        case trees:Trees => receivedTrees(trees)
      }
    }
  }

  //When the users sends the login form, send it as a username to the server
  document.getElementById("login-form").asInstanceOf[html.Form].onsubmit = { event:Event =>
    //Connect to the websocket server
    connect()

    //Swap front and back panes
    document.getElementById("game-div").asInstanceOf[html.Div].className = "fullscreen"
    document.getElementById("start-div").asInstanceOf[html.Div].className = "back fullscreen"

    //Stop drawing the snow as a background
    snowLoop.foreach { id => window.clearInterval(id)}

    //Clear screen
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, size.width, size.height)
    ctx.fill()

    //Create test user
    drawSled("asd", GameClientProtocol.Position(200, 200))

    //Do not redirect
    false
  }

  def main(): Unit = {
    //Start the background loop
    snowLoop = Some(window.setInterval(draw _, 10))
  }

  //Draw a sled at an x and y
  def drawSled(name: String, pos: GameClientProtocol.Position): Unit = {
    val x = pos.x
    val y = pos.y

    //Global strokeStyle
    ctx.strokeStyle = "rgb(100, 100, 100)"

    //Draw two skis
    ctx.lineWidth = 18.0
    ctx.beginPath()
    ctx.moveTo(x-50, y-125)
    ctx.lineTo(x-50, y+125)
    ctx.stroke()
    ctx.beginPath()
    ctx.moveTo(x+50, y-125)
    ctx.lineTo(x+50, y+125)
    ctx.stroke()

    //Draw the barrel for snowballs
    ctx.lineWidth = 2.0
    ctx.fillStyle = "rgb(153, 153, 153)"
    ctx.beginPath()
    ctx.fillRect(x-15, y-97, 30, 100)
    ctx.strokeRect(x-15, y-97, 30, 100)
    ctx.fill()
    ctx.stroke()

    //Draw the main body
    ctx.fillStyle = "rgb(120, 201, 44)"
    ctx.beginPath()
    ctx.arc(x, y, 59, 0, 2*Math.PI)
    ctx.fill()
    ctx.stroke()

    //Draw the name
    ctx.font="30px Arial";
    ctx.beginPath()
    ctx.fillText(name, x-(ctx.measureText(name).width/2), y-135)
    ctx.fill()
  }

  //Draw a tree on the canvas
  def drawTree(pos: GameClientProtocol.Position): Unit = { }

  //When the client recieves the state of canvas, draw all sleds
  def receivedState(state:State): Unit = {
    console.log(s"received state: $state")
    state.sleds.map { sled =>
      drawSled(sled.user.name,sled.position)
    }
  }

  //When the client recieves all trees, draw them
  //TODO: Save the trees
  def receivedTrees(trees:Trees): Unit = {
    console.log(s"received trees: $trees")
    trees.trees.map { tree =>
      drawTree(tree.position)
    }
  }

  def receivedPlayField(playField:PlayField): Unit = {
    console.log(s"received playField: $playField")
  }
}
