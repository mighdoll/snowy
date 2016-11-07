package snowy.connection

import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}
import boopickle.Default._
import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import snowy.sleds._
import snowy.client.LoginScreen
import snowy.connection.GameState._
import upickle.default._
import snowy.playfield.Picklers._
import vector.Vec2d

class InboundEvents(socket: NetworkSocket,
                    sendMessage: (GameServerMessage) => Unit,
                    name: String,
                    kind: SledKind) {
  socket.onOpen { _ =>
    sendMessage(Join(name, kind))
  }

  socket.onError { event =>
    console.log(s"Failed: code: $event")
  }

  def arrayBufferMessage(arrayBuffer: ArrayBuffer): Unit = {
    val byteBuffer = TypedArrayBuffer.wrap(arrayBuffer)
    val message    = Unpickle[GameClientMessage].fromBytes(byteBuffer)
    handleMessage(message)
  }

  socket.onClose { _ =>
    console.log(s"socket closed ")
    LoginScreen.startPanel()
  }

  socket.onMessage { event =>
    event.data match {
      case arrayBuffer: ArrayBuffer => arrayBufferMessage(arrayBuffer)
      case msgString: String        => stringMessage(msgString)
      case x                        => console.log(s"unexpected message received: $x")
    }
  }

  private def handleMessage(message: GameClientMessage): Unit = {
    message match {
      case state: State                => receivedState(state)
      case Playfield(width, height)    => gPlayField = Vec2d(width, height)
      case trees: Trees                => serverTrees = serverTrees.addItems(trees.trees)
      case Died                        => LoginScreen.rejoinPanel()
      case Ping                        => sendMessage(Pong)
      case GameTime(time, oneWayDelay) => // console.log(s"Game Time: $time, $oneWayDelay")
      case newScoreboard: Scoreboard   => scoreboard = newScoreboard
      case x                           => println(s"unexpected message: $message")
    }
  }

  def stringMessage(msg: String): Unit = {
    handleMessage(read[GameClientMessage](msg))
  }

}
