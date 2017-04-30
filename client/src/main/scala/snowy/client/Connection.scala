package snowy.client

import scala.concurrent.duration._
import scala.scalajs.js.typedarray.TypedArrayBufferOps._
import boopickle.Default._
import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameServerProtocol._
import snowy.connection.{InboundEvents, OutboundEvents}
import snowy.playfield.{SkiColor, SledKind}

class Connection() {
  val socket = {
    val inDelay  = 0 milliseconds
    val outDelay = 0 milliseconds
    val protocol =
      if (window.location.protocol == "https:") "wss:" else "ws:"
    val url = s"$protocol//${window.location.host}/game"
    new NetworkSocket(url, inDelay, outDelay)
  }

  def reSpawn(): Unit = {
    sendMessage(ReJoin)
    document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
    document.getElementById("login-div").asInstanceOf[html.Div].classList.add("hide")
  }

  def join(name: String, kind: SledKind, color: SkiColor): Unit = {
    sendMessage(Join(name, kind, color))
  }

  new InboundEvents(socket, sendMessage)

  def sendMessage(item: GameServerMessage): Unit = {
    val bytes     = Pickle.intoBytes(item)
    val byteArray = bytes.typedArray().subarray(bytes.position, bytes.limit)
    socket.socket.send(byteArray.buffer)
  }
}
