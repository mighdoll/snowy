package snowy.client

import scala.scalajs.js.JSApp

object ClientMain extends JSApp {
  def main(): Unit = {
    LoginScreen.startPanel()
    CDraw2.all()
  }
}
