package snowy.client

import org.scalajs.dom.window
import snowy.connection.GameState.{nextState, nextTimeSlice}

import scala.scalajs.js.JSApp

object ClientMain extends JSApp {
  private val drawPlayfield = new DrawPlayfield()
  private val loginScreen   = new LoginScreen()
  private val animation     = new Animation(animate)

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

  def startRedraw(): Unit = animation.start()

  def stopRedraw(): Unit = animation.cancel()

  def death(): Unit = loginScreen.rejoinPanel()
}

/** Manage a function that's run on the browser's requestAnimationFrame loop */
class Animation(frameFn: Double => Unit) {
  private var frameId: Option[Int] = None

  def start(): Unit = nextFrame()

  def animate(timestamp: Double): Unit = {
    cancel()
    frameFn(timestamp)
    nextFrame()
  }

  def nextFrame(): Unit = { frameId = Some(window.requestAnimationFrame(animate)) }

  def cancel(): Unit = frameId.foreach(window.cancelAnimationFrame)
}