package snowy.connection

import network.NetworkSocket
import org.scalajs.dom._
import snowy.GameClientProtocol._
import snowy.GameServerProtocol._
import GameState._
import upickle.default._
import vector.Vec2d
import snowy.client.ClientMain.loginScreen

class InboundEvents(socket: NetworkSocket, name: String) {
  socket.onOpen { _ =>
    socket.send(write(Join(name)))
    switch(true)
  }

  socket.onError { event =>
    console.log(s"Failed: code: $event")
  }

  def switch(game: Boolean){
    game match {
      case true =>
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
    val msg = event.data.toString
    try {
      read[GameClientMessage](msg) match {
        case state: State                => receivedState(state)
        case playfield: Playfield        => gPlayField = Vec2d(playfield.width, playfield.height)
        case trees: Trees                => serverTrees = serverTrees.addItems(trees.trees)
        case Died                        => switch(false); loginScreen.setup()
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
