package snowy.client

import org.scalajs.dom._
import snowy.client.ClientDraw._
import snowy.connection.GameState
import snowy.draw.{DrawSled, SnowFlake}
import snowy.sleds.BasicSled
import vector.Vec2d

object LoginScreen {

  /** Create a loop that moves and draws the snowflakes every 10ms */
  private val snowFlakes = (1 to size.x.toInt / 10).map { i =>
    new SnowFlake(i * 10)
  }
  private val tick = () => {
    clearScreen()

    snowFlakes.foreach { flake =>
      flake.move()
      flake.draw()
    }

    new DrawSled(
      "",
      Vec2d(size.x / 2, size.y / 2),
      size.y * 2 / 3,
      1,
      Math.PI * 3 / 2,
      Math.PI / 2,
      BasicSled,
      "rgb(120, 201, 44)")
  }

  var connected: Option[Connection] = None
  var drawLoop: Option[Int]         = None

  def startPanel() {
    switch(false)

    connected = None
    
    GameState.stopRedraw()
    drawLoop = Some(window.setInterval(tick, 10))
  }

  def switch(game: Boolean) {
    game match {
      case true =>
        document
          .getElementById("game-div")
          .asInstanceOf[html.Div]
          .classList
          .remove("back")
        document
          .getElementById("login-form")
          .asInstanceOf[html.Div]
          .classList
          .add("hide")
      case false =>
        document.getElementById("game-div").asInstanceOf[html.Div].classList.add("back")
        document
          .getElementById("login-form")
          .asInstanceOf[html.Div]
          .classList
          .remove("hide")
    }
  }

  //When the users sends the login form, send it as a username to the server
  document.getElementById("login-form").asInstanceOf[html.Form].onsubmit = {
    event: Event =>
      //Connect to the WebSocket server
      connected match {
        case x if x.isEmpty =>
          connected = Some(
            new Connection(
              document.getElementById("username").asInstanceOf[html.Input].value))
        case x if x.isDefined => connected.get.reSpawn()
        case _                =>
      }

      switch(true)

      //Stop drawing the snow as a background
      drawLoop.foreach(id => window.clearInterval(id))
      GameState.startRedraw()
      //Do not redirect
      false
  }

  def rejoinPanel() {
    switch(false)

    GameState.stopRedraw()
    drawLoop = Some(window.setInterval(tick, 10))
  }
}
