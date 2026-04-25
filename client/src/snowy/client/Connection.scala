package snowy.client

import upickle.default.writeBinary

import network.NetworkSocket
import org.scalajs.dom.*
import snowy.GameServerProtocol.*
import snowy.connection.{GameState, InboundEvents}
import snowy.playfield.{SkiColor, SledType}

import java.nio.ByteBuffer
import scala.concurrent.duration.*
import scala.language.postfixOps
import scala.scalajs.js.typedarray.TypedArrayBufferOps.*
import scala.scalajs.js.typedarray.{ArrayBuffer, Int8Array}
import scala.scalajs.js.JSConverters._

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
    val bytes                = writeBinary[GameServerMessage](item)
    val byteArray: Int8Array = new Int8Array(bytes.toJSArray)
    val buffer: ArrayBuffer  = byteArray.buffer
    socket.socket.send(buffer)
  }
}
