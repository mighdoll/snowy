package snowy.connection

import boopickle.Default._
import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import snowy.client.{ClientMain, UpdateScoreboard}
import snowy.playfield.Picklers._
import vector.Vec2d

import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}

class InboundEvents(gameState: GameState,
                    socket: NetworkSocket,
                    sendMessage: (GameServerMessage) => Unit) {

  def arrayBufferMessage(arrayBuffer: ArrayBuffer): Unit = {
    val byteBuffer = TypedArrayBuffer.wrap(arrayBuffer)
    val message    = Unpickle[GameClientMessage].fromBytes(byteBuffer)
    handleMessage(message)
  }

  socket.onOpen { _ =>
    new OutboundEvents(gameState, sendMessage)
    gameState.serverGameClock = Some(new ServerGameClock(sendMessage))
  }

  socket.onError { event =>
    window.alert(s"Failed: code: $event")
  }

  socket.onClose { event =>
    if (document.hasFocus())
      window.alert(s"Socket closed: $event. The server is probably dead. Reloading")
    window.location.reload()
  }

  socket.onMessage { event =>
    event.data match {
      case arrayBuffer: ArrayBuffer => arrayBufferMessage(arrayBuffer)
      case x                        => console.log(s"unexpected message received: $x")
    }
  }

  private def handleMessage(message: GameClientMessage): Unit = {
    message match {
      case state: State                   => gameState.receivedState(state)
      case PlayfieldBounds(width, height) => gameState.gPlayField = Vec2d(width, height)
      case trees: Trees                   => gameState.serverTrees = trees.trees.toSet
      case Died                           => ClientMain.death()
      case Ping                           => sendMessage(Pong)
      case ClientPong                     => // currently used only by the load test client
      case GameTime(time, oneWayDelay)    => updateClock(time, oneWayDelay)
      case MySled(sledId)                 => gameState.mySledId = Some(sledId)
      case SnowballDeaths(balls)       => gameState.removeSnowballs(balls)
      case SledDeaths(sleds)           => gameState.removeSleds(sleds)
      case newScoreboard: Scoreboard   => ClientMain.updateScoreboard(newScoreboard)
    }
  }

  private def updateClock(time: Long, oneWayDelay: Int): Unit = {
    gameState.serverGameClock.foreach(_.updateClock(time, oneWayDelay))
  }
}
