package snowy.connection

import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}
import boopickle.Default._
import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import snowy.client.{ClientMain, UpdateScoreboard}
import snowy.connection.GameState._
import snowy.draw.{ThreeSleds, ThreeSnowballs}
import snowy.playfield.Picklers._
import snowy.playfield.{SkiColor, SledKind}
import vector.Vec2d

class InboundEvents(socket: NetworkSocket, sendMessage: (GameServerMessage) => Unit) {

  def arrayBufferMessage(arrayBuffer: ArrayBuffer): Unit = {
    val byteBuffer = TypedArrayBuffer.wrap(arrayBuffer)
    val message    = Unpickle[GameClientMessage].fromBytes(byteBuffer)
    handleMessage(message)
  }

  socket.onOpen { _ =>
    new OutboundEvents(sendMessage)
    GameState.serverGameClock = Some(new ServerGameClock(sendMessage))
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
      case state: State                => receivedState(state)
      case Playfield(width, height)    => gPlayField = Vec2d(width, height)
      case trees: Trees                => serverTrees = trees.trees.toSet
      case Died                        => ClientMain.death()
      case Ping                        => sendMessage(Pong)
      case ClientPong                  => // currently used only by the load test client
      case GameTime(time, oneWayDelay) => updateClock(time, oneWayDelay)
      case MySled(sledId)              => mySledId = Some(sledId)
      case SnowballDeaths(balls)       => GameState.removeSnowballs(balls)
      case SledDeaths(sleds)           => GameState.removeSleds(sleds)
      case newScoreboard: Scoreboard   => UpdateScoreboard.updateScoreboard(newScoreboard)
    }
  }

  private def updateClock(time: Long, oneWayDelay: Int): Unit = {
    GameState.serverGameClock.foreach(_.updateClock(time, oneWayDelay))
  }
}
