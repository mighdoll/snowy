package snowy.client

import java.nio.ByteBuffer
import boopickle.DefaultBasic.Pickle
import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameServerProtocol._
import snowy.connection.{GameState, InboundEvents}
import snowy.playfield.Picklers._
import snowy.playfield.{SkiColor, SledType}
import scala.concurrent.duration._
import scala.scalajs.js.typedarray.{ArrayBuffer, Int8Array}
import scala.scalajs.js.typedarray.TypedArrayBufferOps._
import scala.language.postfixOps

class Connection(gameState: GameState) {
  val socket: NetworkSocket = {
    val inDelay  = 0 milliseconds
    val outDelay = 0 milliseconds
    val protocol =
      if (window.location.protocol == "https:") "wss:" else "ws:"
    val url = s"$protocol//${window.location.host}/game"
    new NetworkSocket(url, inDelay, outDelay)
  }

  new InboundEvents(gameState, socket, sendMessage)

  def reSpawn(): Unit = {
    sendMessage(ReJoin)
    document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
    document.getElementById("login-div").asInstanceOf[html.Div].classList.add("hide")
  }

  def join(name: String, sledType: SledType, color: SkiColor): Unit = {
    sendMessage(Join(name, sledType, color))
  }

  def sendMessage(item: GameServerMessage): Unit = {
    val bytes: ByteBuffer    = Pickle.intoBytes(item)
    val byteArray: Int8Array = bytes.typedArray()
    val buffer: ArrayBuffer  = byteArray.buffer.slice(bytes.position, bytes.limit)
    socket.socket.send(buffer)
  }

}
