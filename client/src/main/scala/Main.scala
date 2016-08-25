import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._


import GameServerMessages._
import GameClientMessages._

object TryMe extends JSApp {
  val gameCanvas = document.getElementById("game-c").asInstanceOf[html.Canvas]
  val ctx = gameCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  def main(): Unit = {

    gameCanvas.width = window.innerWidth
    gameCanvas.height = window.innerHeight

    ctx.moveTo(10,10)
    ctx.lineTo(20,20)

    ctx.stroke()

    val socket = new WebSocket("ws://localhost:2345/game")
    val join = Join("Emmett")
    val msg = join.asJson.spaces2
    console.log("main")
    socket.onopen = {event:Event =>
      console.log("socket open")
      socket.send(msg)
    }
    socket.onerror = { event: ErrorEvent =>
      console.log(s"Failed: code: ${event}")
    }
    socket.onmessage = { event: MessageEvent =>
      val msg = event.data.toString
      console.log(s"received message: $msg")
      ClientProtocol.decodeMessage(msg) match {
        case Some(state:State) => receivedState(state)
        case None => console.log(s"unexpected message: $msg")
      }
    }
    socket.onclose = { event: Event =>
      console.log(s"socket closed ")
    }

  }

  def receivedState(state:State): Unit = {
    console.log(s"received state: $state")
  }
}
