package snowy.connection

import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import GameState._
import upickle.default._

class InboundEvents(socket: NetworkSocket, name: String) {
  socket.onOpen { _ =>
    socket.send(write(Join(name)))
    document.getElementById("game-div").asInstanceOf[html.Div].classList.remove("back")
    document.getElementById("login-form").asInstanceOf[html.Div].classList.add("hide")
  }

  socket.onError { event =>
    console.log(s"Failed: code: $event")
  }

  socket.onClose { _ =>
    console.log(s"socket closed ")

    document.getElementById("game-div").asInstanceOf[html.Div].classList.add("back")
    document.getElementById("login-form").asInstanceOf[html.Div].classList.remove("hide")
  }

  socket.onMessage { event =>
    val msg = event.data.toString
    try {
      read[GameClientMessage](msg) match {
        case state: State                => receivedState(state)
        case playfield: Playfield        => gPlayField = playfield
        case trees: Trees                => serverTrees = createStore(trees.trees)
        case Died                        => console.log("ToDo: sled's dead, deal with it.")
        case Ping                        => socket.send(write(Pong))
        case GameTime(time, oneWayDelay) => console.log(s"Game Time: $time, $oneWayDelay")
        case scoreboard: Scoreboard      => // console.log(s"scoreboard: $scoreboard")
      }
    } catch {
      case e: Exception =>
        console.log(s"unexpected message: $msg, ($e)")
    }
  }
}
