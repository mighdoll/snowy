package snowy.client

import minithree.THREE
import minithree.THREE.{WebGLRenderer, WebGLRendererParameters}
import org.scalajs.dom.{document, window}
import snowy.GameClientProtocol.Scoreboard
import snowy.client.login.LoginScreen
import snowy.connection.GameState
import snowy.playfield.{SkiColor, SledType}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.scalajs.js.{Dynamic, JSApp}

object ClientMain extends JSApp {
  val loadedGeometry            = new GeometryLoader()
  var gameScreenActive: Boolean = false

  private val promisedConnection = Promise[Connection]()
  private val renderer           = createRenderer()
  private val loginScreen =
    new LoginScreen(renderer, loadedGeometry.threeGroupsFuture.map(_.threeSleds))
  private var drawPlayfieldOpt: Option[DrawPlayfield]       = None
  private var gameStateOpt: Option[GameState]               = None
  private var updateScoreboardOpt: Option[UpdateScoreboard] = None

  loadedGeometry.threeGroupsFuture.foreach(initializeGame)

  def getWidth(): Double   = window.innerWidth
  def getHeight(): Double  = window.innerHeight
  def gameState: GameState = gameStateOpt.get

  private def createRenderer(): WebGLRenderer = {
    val renderer = new THREE.WebGLRenderer(
      Dynamic
        .literal(antialias = false, canvas = document.getElementById("game-c"))
        .asInstanceOf[WebGLRendererParameters]
    )
    renderer.setClearColor(new THREE.Color(0xe7f1fd), 1)
    renderer.setPixelRatio(
      if (!window.devicePixelRatio.isNaN) window.devicePixelRatio else 1
    )
    renderer.setSize(getWidth(), getHeight())

    renderer
  }

  def main(): Unit = {}

  // Wait until geometries and threesleds are loaded, then initialize the game variables
  def initializeGame(geometries: LoadedGeometries): Unit = {
    val drawPlayfield = new DrawPlayfield(
      renderer,
      geometries.threeSleds,
      geometries.threeSnowballs,
      geometries.threePowerups
    )
    val gameState        = new GameState(drawPlayfield)
    val updateScoreboard = new UpdateScoreboard(gameState)

    drawPlayfieldOpt = Some(drawPlayfield)
    gameStateOpt = Some(gameState)
    updateScoreboardOpt = Some(updateScoreboard)

    val connection = new Connection(gameState)
    // expose the connection only when connected
    val futureConnection = connection.socket.future.map(_ => connection)
    promisedConnection.completeWith(futureConnection)
  }

  def updateScoreboard(scoreboard: Scoreboard): Unit =
    updateScoreboardOpt.foreach(_.updateScoreboard(scoreboard))

  def connectedToServer: Future[Unit] = promisedConnection.future.map(_ => ())

  def freshStartGame(name: String, sledType: SledType, color: SkiColor): Unit = {
    promisedConnection.future.foreach { connection =>
      connection.join(name, sledType, color)
      startGame()
    }
  }

  def rejoinGame(): Unit = {
    promisedConnection.future.foreach { connection =>
      connection.reSpawn()
      startGame()
    }
  }

  def startGame(): Unit = {
    gameStateOpt.foreach(_.startRedraw())
    gameScreenActive = true
  }

  def stopGame(): Unit = {
    gameScreenActive = false
    gameStateOpt.foreach(_.stopRedraw())
  }

  def death(): Unit = loginScreen.rejoinPanel()

  def resize(): Unit = {
    renderer.setSize(getWidth(), getHeight())
    renderer.setViewport(0, 0, getWidth(), getHeight())
  }
}

/** Manage a function that's run on the browser's requestAnimationFrame loop */
class Animation(frameFn: Double => Unit) {
  private var frameId: Option[Int] = None

  def animating(): Boolean = frameId.isDefined

  def start(): Unit = nextFrame()

  def animate(timestamp: Double): Unit = {
    frameFn(timestamp)
    nextFrame()
  }

  def nextFrame(): Unit = { frameId = Some(window.requestAnimationFrame(animate)) }

  def cancel(): Unit = {
    frameId.foreach(window.cancelAnimationFrame)
    frameId = None
  }
}
