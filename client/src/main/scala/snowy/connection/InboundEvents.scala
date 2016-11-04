package snowy.connection

import org.scalajs.dom._
import vector.Vec2d
import network.NetworkSocket
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import GameState._
import snowy.client.ClientMain.loginScreen
import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}
import upickle.default._
import boopickle.Default._
import snowy.playfield.Picklers._

class InboundEvents(socket: NetworkSocket, sendMessage: (GameServerMessage) => Unit, name: String) {
  socket.onOpen { _ =>
    sendMessage(Join(name))
    switch(true)
  }

  socket.onError { event =>
    console.log(s"Failed: code: $event")
  }

  def switch(game: Boolean) {
    game match {
      case true  =>
        document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
        document.getElementById("login-form").asInstanceOf[html.Div].classList.add("hide")
      case false =>
        document.getElementById("game-div").asInstanceOf[html.Div].classList.add("back")
        document.getElementById("login-form").asInstanceOf[html.Div].classList.remove("hide")
    }

  }

  socket.onClose { _ =>
    console.log(s"socket closed ")
    switch(false)
  }

  socket.onMessage { event =>
    event.data match {
      case arrayBuffer: ArrayBuffer => arrayBufferMessage(arrayBuffer)
      case msgString: String        => stringMessage(msgString)
      case x => console.log(s"unexpected message received: $x")
    }
  }

  def arrayBufferMessage(arrayBuffer: ArrayBuffer): Unit = {
    val byteBuffer = TypedArrayBuffer.wrap(arrayBuffer)
    val message = Unpickle[GameClientMessage].fromBytes(byteBuffer)
    handleMessage(message)
  }

  def stringMessage(msg: String): Unit = {
    handleMessage(read[GameClientMessage](msg))
  }

  private def handleMessage(message: GameClientMessage): Unit = {
    message match {
      case state: State                => receivedState(state)
      case Playfield(width, height)    => gPlayField = Vec2d(width, height)
      case trees: Trees                => serverTrees = serverTrees.addItems(trees.trees)
      case Died                        => switch(false); loginScreen.setup()
      case Ping                        => sendMessage(Pong)
      case GameTime(time, oneWayDelay) => console.log(s"Game Time: $time, $oneWayDelay")
      case newScoreboard: Scoreboard   => scoreboard = newScoreboard
      case x                           => println(s"unexpected message: $message")
    }
  }

}
