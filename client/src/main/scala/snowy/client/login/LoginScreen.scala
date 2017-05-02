package snowy.client.login

import minithree.THREE.WebGLRenderer
import org.scalajs.dom._
import org.scalajs.dom.raw.Event
import snowy.client.Connection
import snowy.connection.GameState
import snowy.draw.ThreeSleds
import snowy.playfield.{BasicSkis, BasicSled, SkiColor, SledKind}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LoginScreen(renderer: WebGLRenderer) {
  private val gameDiv               = document.getElementById("game-div").asInstanceOf[html.Div]
  private val gameHud               = document.getElementById("game-hud").asInstanceOf[html.Div]
  private val chosenSled            = document.getElementById("chosen").asInstanceOf[html.Label]
  private val loginForm             = document.getElementById("login-form").asInstanceOf[html.Form]
  private val loginDiv              = document.getElementById("login-div").asInstanceOf[html.Div]
  private val textInput             = document.getElementById("username").asInstanceOf[html.Input]
  private val playButton            = document.getElementById("play").asInstanceOf[html.Button]
  var skiColor: SkiColor            = BasicSkis
  var sledKind: SledKind            = BasicSled
  private val connected: Connection = new Connection()
  private val connectedFuture       = connected.socket.future
  private var spawned: Boolean      = false

  private val loginGeometries =
    new LoginGeometries(renderer, clearConnection, loginScreenActive)

  private var rejoinScreen = false

  Future.sequence(Seq(connectedFuture, loginGeometries.geometriesLoaded)).foreach { _ =>
    playButton.disabled = false
    playButton.innerHTML = "Join Game"
  }

  val threeSleds: Future[ThreeSleds] = loginGeometries.threeSledsFuture

  def clearConnection(): Unit = { spawned = false }

  def loginPressed(e: Event): Unit = {
    e.preventDefault()
    if (!spawned) connected.join(textInput.value, sledKind, skiColor)
    else connected.reSpawn()

    spawned = true

    swapScreen(true)

    rejoinScreen = false
    gameHud.classList.remove("hide")
    loginGeometries.drawPlayfield.foreach(_.startRedraw())
  }

  def rejoinPanel() {
    swapScreen(false)

    loginGeometries.drawPlayfield.foreach(_.stopRedraw())
    gameHud.classList.add("hide")
    playButton.focus()
    rejoinScreen = true

    loginGeometries.renderLoginScreen()
  }

  def loginScreenActive(): Boolean = GameState.mySledId.isEmpty || rejoinScreen

  /** Swap the login screen and game panel */
  def swapScreen(game: Boolean) {
    if (game) {
      gameDiv.classList.remove("back")
      loginDiv.classList.add("hide")
    } else {
      gameDiv.classList.add("back")
      loginDiv.classList.remove("hide")
    }
  }

  window.addEventListener("mousemove", loginGeometries.selectorHover)
  window.addEventListener(
    "mousedown", { _: Event =>
      sledKind = loginGeometries.updateSledSelector(sledKind)
      loginGeometries.updateColors(skiColor).foreach(skiColor = _)
      loginGeometries.updateSelector(sledKind, skiColor)
      chosenSled.innerHTML = "Sled: " + sledKind.toString.replace("Sled", "")
    }
  )

  def setup(): Unit = {
    loginGeometries.setup()
    textInput.focus()
  }
  setup()

  loginForm.addEventListener("submit", loginPressed)
}
