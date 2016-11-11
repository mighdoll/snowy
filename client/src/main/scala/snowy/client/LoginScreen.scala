package snowy.client

import org.scalajs.dom._
import snowy.client.ClientDraw._
import snowy.connection.GameState
import snowy.draw.GameColors.lineColors
import snowy.draw.{DrawSled, SnowFlake}
import snowy.playfield._
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

    DrawSled.draw(
      "",
      Vec2d(size.x / 2, size.y / 2),
      size.y * 1 / 3,
      1,
      Math.PI * 3 / 2,
      Math.PI / 2,
      sledKind,
      skiColor.color.toString,
      "rgb(120, 201, 44)")
  }

  private var connected: Option[Connection] = None
  private var drawLoop: Option[Int]         = None

  private var skiColor: SkiColor            = BasicSkis
  private val skiColors: Seq[SkiColor] =
    Seq(BasicSkis, GreenSkis, RedSkis, YellowSkis)

  private var sledKind: SledKind            = BasicSled
  private val sledKinds: Seq[SledKind] =
    Seq(BasicSled, GunnerSled, TankSled, SpeedySled, SpikySled)

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

  val classText = document.querySelector("#class > .caption").asInstanceOf[html.Div]
  document.querySelector("#class > .container > :first-child").asInstanceOf[html.Span].onclick = { event: Event =>
    val currentIndex = sledKinds.indexOf(sledKind)
    sledKind =
      if (currentIndex < sledKinds.length - 1) sledKinds(currentIndex + 1)
      else sledKinds.head
    classText.innerHTML = "Class: " + sledKind.toString.replace("Sled", "")
  }
  document.querySelector("#class > .container :last-child").asInstanceOf[html.Span].onclick = { event: Event =>
    val currentIndex = sledKinds.indexOf(sledKind)
    sledKind = if (currentIndex > 0) sledKinds(currentIndex - 1) else sledKinds.last
    classText.innerHTML = "Class: " + sledKind.toString.replace("Sled", "")
  }

  val colorText = document.querySelector("#skis > .caption").asInstanceOf[html.Div]
  document.querySelector("#skis > .container > :first-child").asInstanceOf[html.Span].onclick = { event: Event =>
    val currentIndex = skiColors.indexOf(skiColor)
    skiColor =
      if (currentIndex < skiColors.length - 1) skiColors(currentIndex + 1)
      else skiColors.head
    colorText.innerHTML = "Color: " + sledKind.toString.replace("Sled", "")
  }
  document.querySelector("#skis > .container :last-child").asInstanceOf[html.Span].onclick = { event: Event =>
    val currentIndex = skiColors.indexOf(skiColor)
    skiColor = if (currentIndex > 0) skiColors(currentIndex - 1) else skiColors.last
    colorText.innerHTML = "Color: " + skiColor.toString.replace("Skis", "")
  }

  //When the users sends the login form, send it as a username to the server
  document.getElementById("login-form").asInstanceOf[html.Form].onsubmit = {
    event: Event =>
      //Connect to the WebSocket server
      connected match {
        case x if x.isEmpty =>
          connected = Some(
            new Connection(
              document.getElementById("username").asInstanceOf[html.Input].value,
              sledKind,
              skiColor
            )
          )
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
