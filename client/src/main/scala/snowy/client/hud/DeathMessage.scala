package snowy.client.hud

import org.scalajs.dom._

class DeathMessage {
  private val notifications =
    document.getElementById("notifications").asInstanceOf[html.Div]

  private def createHtml(killed: String): String = {
    s"<div class='message'><b>You Iced <div class='kill'>$killed</div></b></div>"
  }

  def killedSled(killed: String): Unit = {
    notifications.innerHTML = createHtml(killed)
  }
}
