package snowy.client.login

import minithree.THREE.WebGLRenderer
import org.scalajs.dom._
import org.scalajs.dom.raw.Event
import snowy.client.ClientMain
import snowy.draw.ThreeSleds
import snowy.playfield.{BasicSkis, BasicSled, SkiColor, SledKind}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LoginScreen(renderer: WebGLRenderer, threeSledsFuture: Future[ThreeSleds]) {
  private val gameDiv          = document.getElementById("game-div").asInstanceOf[html.Div]
  private val gameHud          = document.getElementById("game-hud").asInstanceOf[html.Div]
  private val chosenSled       = document.getElementById("chosen").asInstanceOf[html.Label]
  private val loginForm        = document.getElementById("login-form").asInstanceOf[html.Form]
  private val loginDiv         = document.getElementById("login-div").asInstanceOf[html.Div]
  private val textInput        = document.getElementById("username").asInstanceOf[html.Input]
  private val playButton       = document.getElementById("play").asInstanceOf[html.Button]
  var skiColor: SkiColor       = BasicSkis
  var sledKind: SledKind       = BasicSled
  // True if the ingame sled has the sledkind and color that is currently selected in the ui
  private var sledSelected: Boolean = false

  private val loginGeometries =
    new LoginGeometries(
      renderer,
      clearConnection,
      !ClientMain.gameScreenActive,
      threeSledsFuture
    )

  ClientMain.connectedToServer.foreach { _ =>
    playButton.disabled = false
    playButton.innerHTML = "Join Game"
  }

  def clearConnection(): Unit = { sledSelected = false }

  def loginPressed(e: Event): Unit = {
    e.preventDefault()
    if (!sledSelected) {
      sledSelected = true
      ClientMain.freshStartGame(textInput.value, sledKind, skiColor)
    } else {
      ClientMain.rejoinGame()
    }

    swapScreen(true)

    gameHud.classList.remove("hide")
  }

  def rejoinPanel() {
    swapScreen(false)

    ClientMain.stopGame()
    gameHud.classList.add("hide")
    playButton.focus()

    loginGeometries.renderLoginScreen()
  }

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
