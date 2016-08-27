import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom._
import upickle.default._

import GameServerProtocol._
import GameClientProtocol._

object TryMe extends JSApp {
  var time = 0
  val socket = new WebSocket("ws://localhost:2345/game")
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
  object size {
    val width = window.innerWidth
    val height = window.innerHeight
  }

  def draw() = {
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

  def setup() = {
    gameCanvas.width = size.width
    gameCanvas.height = size.height

    val join = Join("Emmett")
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
  }

  def main(): Unit = {
    setup()
    window.setInterval(draw _, 10)
  }

  socket.onmessage = { event: MessageEvent =>
    val msg = event.data.toString
    console.log(s"received message: $msg")

    read[GameClientMessage](msg) match {
      case state:State => receivedState(state)
    }
  }

  def receivedState(state:State): Unit = {
    console.log(s"received state: $state")
  }
}
