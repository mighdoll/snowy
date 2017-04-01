package snowy.client

import org.scalajs.dom.window
import snowy.connection.GameState.{nextState, nextTimeSlice}

import scala.scalajs.js.JSApp

object ClientMain extends JSApp {
  private val drawPlayfield         = new DrawPlayfield()
  private val loginScreen           = new LoginScreen()
  private var gameLoop: Option[Int] = None
  def main(): Unit = {
    loginScreen.setup()
  }
  def startRedraw(): Unit = {
    stopRedraw()
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

      gameLoop = Some(window.requestAnimationFrame(animate))
    }
    gameLoop = Some(window.requestAnimationFrame(animate))
  }

  def stopRedraw(): Unit = gameLoop.foreach(id => window.cancelAnimationFrame(id))

  def setupPlayfield(): Unit = {
    drawPlayfield.setup()
  }
  def death(): Unit = {
    loginScreen.rejoinPanel()
  }
}
