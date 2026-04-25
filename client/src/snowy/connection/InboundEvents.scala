package snowy.connection

import network.NetworkSocket
import org.scalajs.dom.*
import snowy.GameClientProtocol.*
import snowy.GameServerProtocol.*
import snowy.client.ClientMain
import snowy.client.hud.{AchievementMessage, DeathMessage}
import snowy.playfield.{PlayId, PowerUp, Sled, Snowball}
import upickle.default.readBinary
import vector.Vec2d

import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}

class InboundEvents(
      gameState: GameState,
      socket: NetworkSocket,
      sendMessage: (GameServerMessage) => Unit
) {

  val deathMessage       = new DeathMessage()
  val achievementMessage = new AchievementMessage()

  def arrayBufferMessage(arrayBuffer: ArrayBuffer): Unit = {
    val byteBuffer = TypedArrayBuffer.wrap(arrayBuffer)
    val message    = readBinary[GameClientMessage](byteBuffer)
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
      case trees: InitialTrees            => gameState.serverTrees = trees.trees.toSet
      case Died                           => ClientMain.death()
      case Ping                           => sendMessage(Pong)
      case ClientPong                   => // currently used only by the load test client
      case GameTime(time, oneWayDelay)  => updateClock(time, oneWayDelay)
      case MySled(sledId)               => gameState.mySledId = Some(sledId)
      case newScoreboard: Scoreboard    => ClientMain.updateScoreboard(newScoreboard)
      case AddItems(items)              => gameState.addPlayfieldItems(items)
      case RemoveItems(itemType, items) => removeItems(itemType, items)
      case AchievementMessage(bonus, title, desc) =>
        achievementMessage.display(bonus, title, desc)
      case KilledBy(sledId) => println(s"killed by: $sledId") // TODO display on screen
      case KilledSled(sledId) =>
        gameState.sledNameFromId(sledId).foreach(deathMessage.killedSled)
      case NewKing(sledId)         => println(s"new king is: $sledId")
      case RevengeTargets(sledIds) => println(s"revenge by targeting: $sledIds")
    }
  }

  private def removeItems(itemType: SharedItemType, itemIds: Seq[Int]): Unit = {
    itemType match {
      case SnowballItem => gameState.removeSnowballs(itemIds.map(PlayId[Snowball](_)))
      case PowerUpItem  => gameState.removePowerUps(itemIds.map(PlayId[PowerUp](_)))
      case SledItem     => gameState.removeSleds(itemIds.map(PlayId[Sled](_)))
    }
  }

  private def updateClock(time: Long, oneWayDelay: Int): Unit = {
    gameState.serverGameClock.foreach(_.updateClock(time, oneWayDelay))
  }
}
