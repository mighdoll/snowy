package snowy.client

import network.NetworkSocket
import org.scalajs.dom._
import snowy.connection.{InboundEvents, OutboundEvents}
import upickle.default._
import snowy.GameServerProtocol._

import scala.concurrent.duration._

class Connection(name: String) {
  val socket = {
    val inDelay = 0 milliseconds
    val outDelay = 0 milliseconds
    val url = s"ws://${window.location.host}/game"
    new NetworkSocket(url, inDelay, outDelay)
  }

  new InboundEvents(socket, name)
  new OutboundEvents(socket)

  def reSpawn(): Unit ={
    socket.send(write(ReJoin))
    document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
    document.getElementById("login-form").asInstanceOf[html.Div].classList.add("hide")
  }
}
