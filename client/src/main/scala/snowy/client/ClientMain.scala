package snowy.client

import minithree.THREE
import minithree.THREE.{WebGLRenderer, WebGLRendererParameters}
import org.scalajs.dom.{document, window}
import snowy.client.login.LoginScreen
import scala.concurrent.ExecutionContext.Implicits.global
import snowy.draw.ThreeSleds

import scala.scalajs.js.{Dynamic, JSApp}

object ClientMain extends JSApp {
  private val renderer           = createRenderer()
  private val loginScreen        = new LoginScreen(renderer)

  def getWidth(): Double   = window.innerWidth
  def getHeight(): Double  = window.innerHeight

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

  def death(): Unit = loginScreen.rejoinPanel()

  def resize(): Unit = {
    renderer.setSize(getWidth(), getHeight())
    renderer.setViewport(0, 0, getWidth(), getHeight())
  }

  def threeSleds(fn: ThreeSleds => Unit): Unit = loginScreen.threeSleds.foreach(fn)
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
