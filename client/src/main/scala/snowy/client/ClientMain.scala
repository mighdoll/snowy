package snowy.client

import minithree.THREE
import minithree.THREE.{WebGLRenderer, WebGLRendererParameters}
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{document, window}
import snowy.connection.GameState.{nextState, nextTimeSlice}

import scala.scalajs.js.{Dynamic, JSApp}

object ClientMain extends JSApp {
  private val renderer           = createRenderer()
  private val drawPlayfield      = new DrawPlayfield(renderer)
  private val loginScreen        = new LoginScreen(renderer)
  private val playfieldAnimation = new Animation(animate)

  def getWidth(): Double  = window.innerWidth
  def getHeight(): Double = window.innerHeight

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

  /** Update the client's playfield objects and draw the new playfield to the screen */
  def animate(timestamp: Double): Unit = {
    val deltaSeconds = nextTimeSlice()
    val newState     = nextState(math.max(deltaSeconds, 0))

    drawPlayfield.drawPlayfield(
      newState.snowballs,
      newState.sleds,
      newState.mySled,
      newState.trees,
      newState.playfield
    )
  }

  def startRedraw(): Unit = playfieldAnimation.start()

  def stopRedraw(): Unit = playfieldAnimation.cancel()

  def death(): Unit = loginScreen.rejoinPanel()

  private def resize(): Unit = {
    renderer.setSize(getWidth(), getHeight())
    renderer.setViewport(0, 0, getWidth(), getHeight())
  }

  window.addEventListener("resize", { _: Event =>
    resize()
  }, false)
}

/** Manage a function that's run on the browser's requestAnimationFrame loop */
class Animation(frameFn: Double => Unit) {
  private var frameId: Option[Int] = None

  def start(): Unit = nextFrame()

  def animate(timestamp: Double): Unit = {
    frameFn(timestamp)
    nextFrame()
  }

  def nextFrame(): Unit = { frameId = Some(window.requestAnimationFrame(animate)) }

  def cancel(): Unit = frameId.foreach(window.cancelAnimationFrame)
}
