package snowy.client

import network.NetworkSocket
import org.scalajs.dom._
import snowy.connection.{InboundEvents, OutboundEvents}
import upickle.default._
import boopickle.Default._
import snowy.GameServerProtocol._
import scala.scalajs.js.typedarray.TypedArrayBufferOps._
import scala.concurrent.duration._

class Connection(name: String) {
  val socket = {
    val inDelay = 0 milliseconds
    val outDelay = 0 milliseconds
    val url = s"ws://${window.location.host}/game"
    new NetworkSocket(url, inDelay, outDelay)
  }

  def sendMessage(item: GameServerMessage): Unit = {
    val bytes = Pickle.intoBytes(item)
    val byteArray = bytes.typedArray().subarray(bytes.position, bytes.limit)
    socket.socket.send(byteArray.buffer)
  }

  new InboundEvents(socket, sendMessage, name)
  new OutboundEvents(sendMessage)

  def reSpawn(): Unit ={
    sendMessage(ReJoin())
    document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
    document.getElementById("login-form").asInstanceOf[html.Div].classList.add("hide")
  }
}
